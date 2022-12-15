package Gr8G1.prac.study.order;

import Gr8G1.prac.study.discount.DiscountPolicy;
import Gr8G1.prac.study.member.Member;
import Gr8G1.prac.study.member.MemberRepository;

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
