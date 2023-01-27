package Gr8G1.prac.auth.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
  private final static int OK = HttpStatus.OK.value();
  private final static int NOT_FOUND = HttpStatus.NOT_FOUND.value();
  private final static int INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();

  private final static String OK_MESSAGE = HttpStatus.OK.getReasonPhrase();
  private final static String NOT_FOUND_MESSAGE = HttpStatus.NOT_FOUND.getReasonPhrase();
  private final static String INTERNAL_SERVER_ERROR_MESSAGE = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();

  private final static String INVALID_ACCESS_TOKEN = "Invalid access token.";
  private final static String INVALID_REFRESH_TOKEN = "Invalid refresh token.";
  private final static String NOT_EXPIRED_TOKEN_YET = "Not expired token yet.";

  private final ApiHeaderResponse header;
  private final Map<String, T> body;

  public static <T> ApiResponse<T> of(HttpStatus status, String key, T value) {
    Map<String, T> body = new HashMap<>() {{ put(key, value); }};

    return new ApiResponse<>(new ApiHeaderResponse(status.value(), status.getReasonPhrase()), body);
  }

  public static <T> ApiResponse<T> ok(String key, T value) {
    Map<String, T> body = new HashMap<>() {{ put(key, value); }};

    return new ApiResponse<>(new ApiHeaderResponse(OK, OK_MESSAGE), body);
  }

  public static <T> ApiResponse<T> notFound() {
    return new ApiResponse<>(new ApiHeaderResponse(NOT_FOUND, NOT_FOUND_MESSAGE), null);
  }

  public static <T> ApiResponse<T> invalidAccessToken() {
    return new ApiResponse<>(new ApiHeaderResponse(INTERNAL_SERVER_ERROR, INVALID_ACCESS_TOKEN), null);
  }

  public static <T> ApiResponse<T> invalidRefreshToken() {
    return new ApiResponse<>(new ApiHeaderResponse(INTERNAL_SERVER_ERROR, INVALID_REFRESH_TOKEN), null);
  }

  public static <T> ApiResponse<T> notExpiredTokenYet() {
    return new ApiResponse<>(new ApiHeaderResponse(INTERNAL_SERVER_ERROR, NOT_EXPIRED_TOKEN_YET), null);
  }
}
