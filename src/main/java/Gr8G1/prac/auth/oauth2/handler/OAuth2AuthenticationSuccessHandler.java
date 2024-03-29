package Gr8G1.prac.auth.oauth2.handler;

import Gr8G1.prac.auth.config.properties.AppProperties;
import Gr8G1.prac.auth.oauth2.entity.ProviderType;
import Gr8G1.prac.auth.oauth2.entity.RoleType;
import Gr8G1.prac.auth.oauth2.info.OAuth2UserInfo;
import Gr8G1.prac.auth.oauth2.info.OAuth2UserInfoFactory;
import Gr8G1.prac.auth.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import Gr8G1.prac.auth.oauth2.token.AuthToken;
import Gr8G1.prac.auth.oauth2.token.AuthTokenProvider;
import Gr8G1.prac.auth.api.user.entity.UserRefreshToken;
import Gr8G1.prac.auth.api.user.repository.UserRefreshTokenRepository;
import Gr8G1.prac.auth.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static Gr8G1.prac.auth.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static Gr8G1.prac.auth.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final AppProperties appProperties;
  private final AuthTokenProvider authTokenProvider;
  private final UserRefreshTokenRepository userRefreshTokenRepository;
  private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String targetUrl = determineTargetUrl(request, response, authentication);

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }

    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                                     .map(Cookie::getValue);

    if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
      throw new IllegalArgumentException(
        "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication"
      );
    }

    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

    OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
    ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

    OidcUser user = (OidcUser) authentication.getPrincipal();
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
    Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();
    RoleType roleType = hasAuthority(authorities, RoleType.ADMIN.getCode()) ? RoleType.ADMIN : RoleType.USER;

    Date now = new Date();

    // # Access Token
    AuthToken accessToken = authTokenProvider.createAuthToken(
      userInfo.getId(),
      roleType.getCode(),
      new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
    );

    // # Refresh Token
    AuthToken refreshToken = authTokenProvider.createAuthToken(
      appProperties.getAuth().getTokenSecret(),
      new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpiry())
    );

    // # DB Access
    UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userInfo.getId());

    if (userRefreshToken != null) {
      userRefreshToken.setRefreshToken(refreshToken.getToken());
    } else {
      userRefreshToken = new UserRefreshToken(userInfo.getId(), refreshToken.getToken());
      userRefreshTokenRepository.saveAndFlush(userRefreshToken);
    }

    int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 60;
    CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
    CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

    return createURI(accessToken, refreshToken).toString();
    // return UriComponentsBuilder.fromUriString(targetUrl)
    //          .queryParam("access_token", accessToken.getToken())
    //          .build()
    //          .toUriString();
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
  }

  private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
    if (authorities == null) {
      return false;
    }

    for (GrantedAuthority grantedAuthority : authorities) {
      if (authority.equals(grantedAuthority.getAuthority())) {
        return true;
      }
    }
    return false;
  }

  private boolean isAuthorizedRedirectUri(String uri) {
    URI clientRedirectUri = URI.create(uri);

    return appProperties.getOauth2().getAuthorizedRedirectUris()
             .stream()
             .anyMatch(authorizedRedirectUri -> {
               // Only validate host and port. Let the clients use different paths if they want to
               URI authorizedURI = URI.create(authorizedRedirectUri);

               return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) &&
                        authorizedURI.getPort() == clientRedirectUri.getPort();
             });
  }

  private URI createURI(AuthToken accessToken, AuthToken refreshToken) {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("access_token", accessToken.getToken());
    queryParams.add("refresh_token", refreshToken.getToken());

    return UriComponentsBuilder.newInstance()
             .scheme("http")
             .host("localhost")
             // .port(80)
             .path("/receive.html")
             .queryParams(queryParams)
             .build()
             .toUri();
  }
}
