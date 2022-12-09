package Gr8G1.prac.order;

public interface OrderService {
  Order createOrder(Long memberId, String itemName, Integer price);
}
