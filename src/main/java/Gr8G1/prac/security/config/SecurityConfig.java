package Gr8G1.prac.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserDetailsService userDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .anyRequest()
      .authenticated();

    // # Form Login
    http.formLogin()
      // 커스텀 로그인 페이지 생성 시 경로 지정
      // .loginPage("/login.html")
      .defaultSuccessUrl("/")
      .failureUrl("/login")
      .loginProcessingUrl("/login")
      .usernameParameter("userName")
      .passwordParameter("password")
      .successHandler(new AuthenticationSuccessHandler() {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
          System.out.println("authentication.getName = " + authentication.getName());

          response.sendRedirect("/");
        }
      })
      .failureHandler(new AuthenticationFailureHandler() {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
          System.out.println("exception = " + exception.getMessage());

          response.sendRedirect("/login");
        }
      })
      .permitAll();

    // # Logout
    http.logout()
      // 커스텀 로그아웃 페이지 생성시 사용
      // .logoutUrl("/logout")
      .logoutSuccessUrl("/login")

      .addLogoutHandler(new LogoutHandler() {
        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
          HttpSession session = request.getSession();
          session.invalidate();
        }
      })
      .logoutSuccessHandler(new LogoutSuccessHandler() {
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
          response.sendRedirect("/login");
        }
      })
      .deleteCookies("remember-me");

    // # Remember Me
    http.rememberMe()
      .rememberMeParameter("rememberMe")
      // Default TWO_WEEKS_S = 1209600
      .tokenValiditySeconds(3600)
      // 리멤버미 기능 강제 활성화
      // .alwaysRemember(true)
      .userDetailsService(userDetailsService);

    return http.build();
  }
}
