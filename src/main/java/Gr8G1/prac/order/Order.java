package Gr8G1.prac.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Order {
  private final Long memberId;
  private final String itemName;
  private final Integer itemPrice;
  private final Integer discountPrice;

  public Integer calcultePrice() {
    return itemPrice - discountPrice;
  }
}
