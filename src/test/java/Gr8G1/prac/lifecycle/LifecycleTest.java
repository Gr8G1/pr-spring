package Gr8G1.prac.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class LifecycleTest {
  @Test
  void networkConnectionTest() {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(LifecycleConfig.class);
    ac.getBean("networkClient");
    ac.close();
  }

  @Configuration
  static class LifecycleConfig {
    @Bean
    // @Bean(initMethod = "connect", destroyMethod = "disconnect")
    NetworkClient networkClient() {
      NetworkClient networkClient = new NetworkClient();
      networkClient.setUrl("http://hello-world.dev");

      return networkClient;
    }
  }
}
