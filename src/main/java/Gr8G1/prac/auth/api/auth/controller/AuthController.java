package Gr8G1.prac.auth.api.auth.controller;

import Gr8G1.prac.auth.api.auth.mapper.AuthMapper;
import Gr8G1.prac.auth.api.user.entity.User;
import Gr8G1.prac.auth.api.user.mapper.UserMapper;
import Gr8G1.prac.auth.api.user.service.UserService;
import Gr8G1.prac.auth.config.properties.AppProperties;
import Gr8G1.prac.auth.oauth2.entity.RoleType;
import Gr8G1.prac.auth.oauth2.entity.UserPrincipal;
import Gr8G1.prac.auth.oauth2.token.AuthToken;
import Gr8G1.prac.auth.oauth2.token.AuthTokenProvider;
import Gr8G1.prac.auth.response.ApiResponse;
import Gr8G1.prac.auth.response.SingleResponse;
import Gr8G1.prac.auth.api.user.dto.UserDto;
import Gr8G1.prac.auth.api.user.entity.UserRefreshToken;
import Gr8G1.prac.auth.api.user.repository.UserRefreshTokenRepository;
import Gr8G1.prac.auth.utils.CookieUtil;
import Gr8G1.prac.auth.utils.HeaderUtil;
import Gr8G1.prac.auth.utils.UriUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private static final String DEFAULT_URI = "/api/v1/auth";

  private final AppProperties appProperties;
  private final AuthTokenProvider tokenProvider;
  private final AuthenticationManager authenticationManager;
  private final UserRefreshTokenRepository userRefreshTokenRepository;
  private final UserService userService;
  private final UserMapper userMapper;

  private final static long THREE_DAYS_MSEC = 259200000;
  private final static String REFRESH_TOKEN = "refresh_token";

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody UserDto.Signup requestBody) {
    User user = userService.createUser(userMapper.userDtoSignupToUser(requestBody));

    URI uri = UriUtil.createUri(DEFAULT_URI, user.getUserSeq());

    return ResponseEntity.created(uri).body(ApiResponse.ok("message", "User registered successfully"));
  }

  @PostMapping("/login")
  public ApiResponse<?> login(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody UserDto.Login requestBody) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        requestBody.getId(),
        requestBody.getPassword()
      )
    );

    String userId = requestBody.getId();
    SecurityContextHolder.getContext().setAuthentication(authentication);
    Date now = new Date();

    // # Access Token 생성
    AuthToken accessToken = tokenProvider.createAuthToken(
      userId,
      ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(),
      new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
    );

    // # Refresh Token 생성
    AuthToken refreshToken = tokenProvider.createAuthToken(
      appProperties.getAuth().getTokenSecret(),
      new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpiry())
    );

    // # DB - Refresh Token 확인
    UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
    if (userRefreshToken == null) {
      userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
      userRefreshTokenRepository.saveAndFlush(userRefreshToken);
    } else {
      userRefreshToken.setRefreshToken(refreshToken.getToken());
    }

    int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;
    CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
    CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

    return ApiResponse.ok("accessToken", accessToken.getToken());
  }

  @GetMapping("/refresh")
  public ApiResponse<?> refreshToken (HttpServletRequest request, HttpServletResponse response) {
    // # Access Token
    String accessToken = HeaderUtil.getAccessToken(request);
    AuthToken authAccessToken = tokenProvider.convertAuthToken(accessToken);

    // # Access Token 검증
    if (!authAccessToken.validate()) return ApiResponse.invalidAccessToken();

    // # Access Token expired 확인
    Claims claims = authAccessToken.getExpiredTokenClaims();
    if (claims == null) return ApiResponse.notExpiredTokenYet();

    String userId = claims.getSubject();
    RoleType roleType = RoleType.of(claims.get("role", String.class));

    // # Refresh Token
    String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN).map(Cookie::getValue).orElse(null);
    AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

    // # Refresh Token 검증
    if (!authRefreshToken.validate()) return ApiResponse.invalidRefreshToken();

    // # DB - Refresh token 확인
    UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
    if (userRefreshToken == null) return ApiResponse.invalidRefreshToken();

    Date now = new Date();

    // # Access Token 신규 생성
    authAccessToken = tokenProvider.createAuthToken(
      userId,
      roleType.getCode(),
      new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
    );

    long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

    // # Refresh token expiry 3일 이하 일시 갱신 처리
    if (validTime <= THREE_DAYS_MSEC) {
      long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

      authRefreshToken = tokenProvider.createAuthToken(
        appProperties.getAuth().getTokenSecret(),
        new Date(now.getTime() + refreshTokenExpiry)
      );

      // DB에 refresh 토큰 업데이트
      userRefreshToken.setRefreshToken(authRefreshToken.getToken());

      int cookieMaxAge = (int) refreshTokenExpiry / 60;
      CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
      CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
    }

    return ApiResponse.ok("access_token", authAccessToken.getToken());
  }
}
