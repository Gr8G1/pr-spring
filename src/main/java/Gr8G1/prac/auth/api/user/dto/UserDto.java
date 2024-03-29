package Gr8G1.prac.auth.api.user.dto;

import Gr8G1.prac.auth.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
  @Getter
  public static class Signup {
    @NotBlank
    private String id;

    @Pattern(
      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_\\-\"',.+/])[A-Za-z\\d!@#$%^&*()_\\-\"',.+/]{8,}$",
      message = "비밀번호는 영어(대/소문자), 숫자, 특수문자 포함 8자 이상으로 구성되어야 합니다."
    )
    private String password;

    @Pattern(regexp = "^\\S+(\\s?\\S+)*$", message = "이름은 공백이 아니어야 합니다.")
    private String name;

    @NotBlank
    @Email
    private String email;

    @Pattern(
      regexp = "^01([016789])-?([0-9]{4})-?([0-9]{4})$",
      message = "휴대폰 번호는 11자리 숫자와 '-'로 구성되어야 합니다."
    )
    private String phone;
  }

  @Getter
  public static class Login {
    @NotBlank
    private String id;

    @NotBlank
    private String password;
  }

  @AllArgsConstructor
  @Getter
  public static class Response {
    private long id;
    private String userId;
    private String name;
    private String email;
    private String emailVerifiedYn;
    private String phone;
    private String profileImageUrl;
    private String providerType;
    private User.MemberStatus memberStatus;
  }
}
