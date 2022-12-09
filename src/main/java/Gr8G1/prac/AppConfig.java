package Gr8G1.prac;

import Gr8G1.prac.discount.DiscountPolicy;
import Gr8G1.prac.discount.FixedDiscountPolicyImpl;
import Gr8G1.prac.discount.RateDiscountPolicyImpl;
import Gr8G1.prac.member.MemberMemoryRepositoryImpl;
import Gr8G1.prac.member.MemberRepository;
import Gr8G1.prac.member.MemberService;
import Gr8G1.prac.member.MemberServiceImpl;
import Gr8G1.prac.order.OrderService;
import Gr8G1.prac.order.OrderServiceImpl;

public class AppConfig {
  public MemberRepository memberRepository() {
    return new MemberMemoryRepositoryImpl();
  }

  public MemberService memberService(MemberRepository memberRepository) {
    return new MemberServiceImpl(memberRepository);
  }

  public OrderService orderService(MemberRepository memberRepository) {
    // 고정 할인
    // return new OrderServiceImpl(memberRepository, fixedDiscountPolicy());
    // 비율 할인
    return new OrderServiceImpl(memberRepository, rateDiscountPolicyImpl());
  }

  public DiscountPolicy fixedDiscountPolicy() {
    return new FixedDiscountPolicyImpl();
  }

  public DiscountPolicy rateDiscountPolicyImpl() {
    return new RateDiscountPolicyImpl();
  }
}
