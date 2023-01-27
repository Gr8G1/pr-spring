package Gr8G1.prac.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiHeaderResponse {
  private int code;
  private String message;
}
