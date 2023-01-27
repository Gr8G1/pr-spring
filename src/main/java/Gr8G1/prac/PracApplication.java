package Gr8G1.prac;

import Gr8G1.prac.auth.config.properties.CorsProperties;
import Gr8G1.prac.auth.config.properties.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(
  // # 예외 처리
  // scanBasePackages = "Gr8G1.prac.exception"

  // # 스프링 시큐리티
  // scanBasePackages = "Gr8G1.prac.security"

  // # 스프링 시큐리티 & JWT
  scanBasePackages = "Gr8G1.prac.auth"
)
@EnableConfigurationProperties({
  CorsProperties.class,
  AppProperties.class
})
public class PracApplication {
  public static void main(String[] args) {
    SpringApplication.run(PracApplication.class, args);
  }
}
