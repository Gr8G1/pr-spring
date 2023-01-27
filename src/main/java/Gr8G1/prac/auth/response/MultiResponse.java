package Gr8G1.prac.auth.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MultiResponse<T> {
  private List<T> data;
  private PageInfoResponse pageInfo;

  public MultiResponse(List<T> data, Page<?> page) {
    this.data = data;
    this.pageInfo = new PageInfoResponse(
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getTotalPages()
    );
  }
}
