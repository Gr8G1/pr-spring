package Gr8G1.prac.auth.config.security;

import Gr8G1.prac.auth.oauth2.token.AuthTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
  @Value("${app.auth.tokenSecret}")
  private String secret;

  @Bean
  public AuthTokenProvider jwtProvider() {
    return new AuthTokenProvider(secret);
  }
}
