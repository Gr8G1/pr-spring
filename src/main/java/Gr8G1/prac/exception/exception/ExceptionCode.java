package Gr8G1.prac.exception.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum ExceptionCode {
  INVALID_INPUT_VALUE(400, "C1", " Invalid input value"),
  ENTITY_NOT_FOUND(400, "C2", " Entity not found"),
  INVALID_TYPE_VALUE(400, "C3", " Invalid type value"),
  HANDLE_ACCESS_DENIED(403, "C4", "Access is denied"),
  METHOD_NOT_ALLOWED(405, "C5", " Method not allowed"),
  INTERNAL_SERVER_ERROR(500, "C6", "Internal server error"),

  MEMBER_EXISTS(409, "C7", "Member exists")
  ;

  private final int status;
  private final String code;
  private final String message;
}
