package Gr8G1.prac.auth.config.security;

import Gr8G1.prac.auth.config.properties.AppProperties;
import Gr8G1.prac.auth.config.properties.CorsProperties;
import Gr8G1.prac.auth.oauth2.entity.RoleType;
import Gr8G1.prac.auth.oauth2.filter.TokenAuthenticationFilter;
import Gr8G1.prac.auth.oauth2.handler.CustomAccessDeniedHandler;
import Gr8G1.prac.auth.oauth2.handler.CustomAuthenticationEntryPoint;
import Gr8G1.prac.auth.oauth2.handler.OAuth2AuthenticationFailureHandler;
import Gr8G1.prac.auth.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import Gr8G1.prac.auth.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import Gr8G1.prac.auth.oauth2.service.CustomOAuth2UserService;
import Gr8G1.prac.auth.oauth2.service.CustomUserDetailService;
import Gr8G1.prac.auth.oauth2.token.AuthTokenProvider;
import Gr8G1.prac.auth.api.user.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
  securedEnabled = true,
  jsr250Enabled = true,
  prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
  private final CorsProperties corsProperties;
  private final AppProperties appProperties;
  private final AuthTokenProvider authTokenProvider;
  private final CustomUserDetailService customUserDetailService;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final UserRefreshTokenRepository userRefreshTokenRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .headers()
          .frameOptions()
          .sameOrigin()
      .and()
        .cors()
      .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .exceptionHandling()
          .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
          .accessDeniedHandler(new CustomAccessDeniedHandler())
      .and()
        .apply(new CustomFilterConfigurer())
      .and()
        .authorizeRequests()
          .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
          .antMatchers("/",
            "/error",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"
          ).permitAll()
          .antMatchers("/api/**/auth/signup", "/api/**/auth/login").permitAll()
          .antMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode())
          .antMatchers("/api/**/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode())
          .anyRequest().authenticated()

      .and()
        .userDetailsService(customUserDetailService)
        .oauth2Login()
          .authorizationEndpoint()
            .baseUri("/oauth2/authorization")
          .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
      .and()
        .redirectionEndpoint()
          .baseUri("/*/oauth2/code/*")
      .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
      .and()
        .successHandler(oAuth2AuthenticationSuccessHandler())
        .failureHandler(oAuth2AuthenticationFailureHandler());

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
                      .antMatchers("/h2/**");
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /*
   * # PasswordEncoder
   *
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /*
   * # 토큰 필터 설정
   *
   */
  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter(authTokenProvider);
  }

  /*
   * # 쿠키 기반 인가 Repository
   *
   */
  @Bean
  public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
    return new OAuth2AuthorizationRequestBasedOnCookieRepository();
  }

  /*
   * # Oauth 인증 성공 핸들러
   *
   */
  @Bean
  public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
    return new OAuth2AuthenticationSuccessHandler(
      appProperties,
      authTokenProvider,
      userRefreshTokenRepository,
      oAuth2AuthorizationRequestBasedOnCookieRepository()
    );
  }

  /*
   * # Oauth 인증 실패 핸들러
   *
   */
  @Bean
  public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
    return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
  }

  /*
   * # Cors
   *
   */
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
    corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
    corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
    corsConfig.setAllowCredentials(true);
    corsConfig.setMaxAge(corsConfig.getMaxAge());

    corsConfigSource.registerCorsConfiguration("/**", corsConfig);
    return corsConfigSource;
  }

  public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
    @Override
    public void configure(HttpSecurity builder) throws Exception {
      builder.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
  }
}
