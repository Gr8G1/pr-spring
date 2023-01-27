package Gr8G1.prac.auth.api.auth.dto;

import lombok.Getter;

@Getter
public class AuthDto {
  public static class Response {
    private String accessToken;
    private String refreshToken;
  }
}
