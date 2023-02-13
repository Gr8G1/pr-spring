package Gr8G1.prac.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TodoDto {
  @Getter
  public static class POST {
    private String title;
    private Integer order;
    private Boolean completed;
  }

  @Getter
  public static class Patch {
    private String title;
    private Integer order;
    private Boolean completed;
  }

  @Getter
  @AllArgsConstructor
  public static class Response {
    private Long id;
    private String title;
    private Integer order;
    private Boolean completed;
    private String url;
  }
}
