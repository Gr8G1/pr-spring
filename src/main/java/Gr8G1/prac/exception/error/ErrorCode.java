package Gr8G1.prac.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
  INVALID_INPUT_VALUE(400, "C1", " Invalid input value"),
  ENTITY_NOT_FOUND(400, "C2", " Entity not found"),
  INVALID_TYPE_VALUE(400, "C3", " Invalid type value"),
  HANDLE_ACCESS_DENIED(403, "C4", "Access is denied"),
  METHOD_NOT_ALLOWED(405, "C5", " Method not allowed"),
  INTERNAL_SERVER_ERROR(500, "C4", "Internal server error"),
  ;

  private final int status;
  private final String code;
  private final String message;

  ErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
