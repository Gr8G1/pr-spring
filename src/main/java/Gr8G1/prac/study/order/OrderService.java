package Gr8G1.prac.study.order;

public interface OrderService {
  Order createOrder(Long memberId, String itemName, Integer price);
}
