package Gr8G1.prac.order;

import Gr8G1.prac.discount.DiscountPolicy;
import Gr8G1.prac.member.Member;
import Gr8G1.prac.member.MemberRepository;

public class OrderServiceImpl implements OrderService {
  private final MemberRepository memberRepository;
  private final DiscountPolicy discountPolicy;

  public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
  }

  @Override
  public Order createOrder(Long memberId, String itemName, Integer price) {
    Member member = memberRepository.findById(memberId);

    Integer discount = discountPolicy.discount(member, price);

    return new Order(memberId, itemName, price, discount);
  }
}
