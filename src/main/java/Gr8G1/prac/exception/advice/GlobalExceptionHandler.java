package Gr8G1.prac.exception.advice;

import Gr8G1.prac.exception.error.ErrorCode;
import Gr8G1.prac.exception.error.ErrorResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  /*
   * # Method Argument Not Valid
   *  - javax.validation.Valid | @Validated 바인딩 오류 여부 확인
   *  - HttpMessageConverter -> HttpMessageConverter 바인딩 오류 여부 확인
   *  - @RequestBody, @RequestPart 사용 유무 확인
   *
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException", e);

    return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
  }

  /*
   * # Method Argument Type Mismatch
   *  - 전달인자값 Type 오류 여부 확인
   *
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    log.error("handleMethodArgumentTypeMismatchException", e);

    final ErrorResponse response = ErrorResponse.of(e);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /*
   * # Http Request Method Not Supported
   *  - 전송 메소드 타입 확인
   *
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    log.error("handleHttpRequestMethodNotSupportedException", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }

  /*
   * # AccessDenied
   *  - Security 예외 Authentication
   *    > 객체가 필요한 권한 보유 유무 확인
   *
   */
  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    log.error("handleAccessDeniedException", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
    return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
  }

  /*
   * # 기타 예외 처리 (최상위)
   *
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("handleEntityNotFoundException", e);

    final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /*
   * # 비지니스 로직 예외 처리 (추가)
   *
   */
  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    log.error("handleEntityNotFoundException", e);

    final ErrorCode errorCode = e.getErrorCode();
    final ErrorResponse response = ErrorResponse.of(errorCode);
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
  }
}

/*
 * # 예외 추가
 *
 */
class BusinessException extends RuntimeException {
  @Getter
  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public BusinessException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}

class EntityNotFoundException extends BusinessException {
  public EntityNotFoundException(String message) {
    super(message, ErrorCode.ENTITY_NOT_FOUND);
  }
}

class InvalidValueException extends BusinessException {
  public InvalidValueException(String value) {
    super(value, ErrorCode.INVALID_INPUT_VALUE);
  }

  public InvalidValueException(String value, ErrorCode errorCode) {
    super(value, errorCode);
  }
}
