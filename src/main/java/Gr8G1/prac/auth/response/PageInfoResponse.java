package Gr8G1.prac.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfoResponse {
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
}
