package Gr8G1.prac.order;

public class Order {
  private final Long memberId;
  private final String itemName;
  private final Integer itemPrice;
  private final Integer discountPrice;

  public Order(Long memberId, String itemName, Integer itemPrice, Integer discountPrice) {
    this.memberId = memberId;
    this.itemName = itemName;
    this.itemPrice = itemPrice;
    this.discountPrice = discountPrice;
  }

  public Long getMemberId() {
    return memberId;
  }

  public String getItemName() {
    return itemName;
  }

  public Integer getItemPrice() {
    return itemPrice;
  }

  public Integer getDiscountPrice() {
    return discountPrice;
  }

  public Integer calcultePrice() {
    return itemPrice - discountPrice;
  }

  @Override
  public String toString() {
    return "Order{" +
        "memberId=" + memberId +
        ", itemName='" + itemName + '\'' +
        ", itemPrice=" + itemPrice +
        ", discountPrice=" + discountPrice +
        '}';
  }
}
