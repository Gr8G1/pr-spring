package Gr8G1.prac.exception.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ErrorDto {
  @Getter
  public static class Post {
    private Long id;

    @Pattern(regexp = "^\\S+(\\s?\\S+)*$", message = "이름은 공백이 아니어야 합니다.")
    private String name;
    @Pattern(regexp = "^01([016789])-?([0-9]{4})-?([0-9]{4})$", message = "휴대폰 번호는 11자리 숫자와 '-'로 구성되어야 합니다.")
    private String phone;

    @NotBlank
    @Email
    private String email;
  }
}
