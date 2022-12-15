package Gr8G1.prac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "Gr8G1.prac.section")
public class PracApplication {
  public static void main(String[] args) {
    SpringApplication.run(PracApplication.class, args);
  }
}
