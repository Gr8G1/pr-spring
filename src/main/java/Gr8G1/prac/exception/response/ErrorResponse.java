package Gr8G1.prac.exception.response;

import Gr8G1.prac.exception.exception.ExceptionCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {
  private final List<FieldError> errors;
  private final int status;
  private final String code;
  private final String message;

  private ErrorResponse(final ExceptionCode code) {
    this.errors = new ArrayList<>();
    this.code = code.getCode();
    this.status = code.getStatus();
    this.message = code.getMessage();
  }

  private ErrorResponse(final ExceptionCode code, final List<FieldError> errors) {
    this.errors = errors;
    this.code = code.getCode();
    this.status = code.getStatus();
    this.message = code.getMessage();
  }

  public static ErrorResponse of(final ExceptionCode code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
    final String value = e.getValue() == null ? "" : e.getValue().toString();
    final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());

    return new ErrorResponse(ExceptionCode.INVALID_TYPE_VALUE, errors);
  }

  public static ErrorResponse of(final ExceptionCode code, final BindingResult bindingResult) {
    return new ErrorResponse(code, FieldError.of(bindingResult));
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FieldError {
    private String field;
    private String value;
    private String reason;

    private FieldError(final String field, final String value, final String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    public static List<FieldError> of(final String field, final String value, final String reason) {
      List<FieldError> fieldErrors = new ArrayList<>();
      fieldErrors.add(new FieldError(field, value, reason));

      return fieldErrors;
    }

    private static List<FieldError> of(BindingResult bindingResult) {
      final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();

      return fieldErrors.stream()
        .map(error -> new FieldError(
          error.getField(),
          error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
          error.getDefaultMessage()))
        .collect(Collectors.toList());
    }
  }
}
