package Gr8G1.prac.auth.exception;

import lombok.Getter;

/*
 * # 예외 추가
 *
 */
public class BusinessException extends RuntimeException {
  @Getter
  private final ExceptionCode exceptionCode;

  public BusinessException(ExceptionCode exceptionCode) {
    super(exceptionCode.getMessage());
    this.exceptionCode = exceptionCode;
  }

  public BusinessException(String message, ExceptionCode exceptionCode) {
    super(message);
    this.exceptionCode = exceptionCode;
  }
}
