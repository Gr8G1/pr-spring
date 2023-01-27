package Gr8G1.prac.auth.oauth2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {
  ADMIN("ROLE_ADMIN", "관리자"),
  USER("ROLE_USER", "일반 사용자"),
  GUEST("GUEST", "게스트");

  private final String code;
  private final String displayName;

  public static RoleType of(String code) {
    return Arrays.stream(RoleType.values())
             .filter(r -> r.getCode().equals(code))
             .findAny()
             .orElse(GUEST);
  }
}
