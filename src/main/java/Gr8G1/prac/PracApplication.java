package Gr8G1.prac;

import Gr8G1.prac.auth.config.properties.CorsProperties;
import Gr8G1.prac.auth.config.properties.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaAuditing
@SpringBootApplication(
  // # 예외 처리
  // scanBasePackages = "Gr8G1.prac.exception"

  // # 스프링 시큐리티
  // scanBasePackages = "Gr8G1.prac.security"

  // # 스프링 시큐리티 & JWT
  // scanBasePackages = "Gr8G1.prac.auth"

  // # Todo Backend
  scanBasePackages = "Gr8G1.prac.todo",
  exclude = { SecurityAutoConfiguration.class }
)
@EnableConfigurationProperties({
  CorsProperties.class,
  AppProperties.class
})
public class PracApplication {
  public static void main(String[] args) {
    SpringApplication.run(PracApplication.class, args);
  }

  // @Bean
  // public WebMvcConfigurer corsConfigurer() {
  //   return new WebMvcConfigurer() {
  //     @Override
  //     public void addCorsMappings(CorsRegistry registry) {
  //       registry.addMapping("/**")
  //         .allowedOrigins("https://todobackend.com/");
  //     }
  //   };
  // }
}
