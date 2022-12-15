package Gr8G1.prac.study;

import Gr8G1.prac.study.discount.DiscountPolicy;
import Gr8G1.prac.study.discount.FixedDiscountPolicyImpl;
import Gr8G1.prac.study.discount.RateDiscountPolicyImpl;
import Gr8G1.prac.study.member.MemberMemoryRepositoryImpl;
import Gr8G1.prac.study.member.MemberRepository;
import Gr8G1.prac.study.member.MemberService;
import Gr8G1.prac.study.member.MemberServiceImpl;
import Gr8G1.prac.study.order.OrderService;
import Gr8G1.prac.study.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  @Bean
  public MemberRepository memberRepository() {
    return new MemberMemoryRepositoryImpl();
  }
  @Bean
  public MemberService memberService() {
    return new MemberServiceImpl(memberRepository());
  }

  @Bean
  public OrderService orderService() {
    // 고정 할인
    // return new OrderServiceImpl(memberRepository(), fixedDiscountPolicy());
    // 비율 할인
    return new OrderServiceImpl(memberRepository(), rateDiscountPolicyImpl());
  }

  @Bean
  public DiscountPolicy fixedDiscountPolicy() {
    return new FixedDiscountPolicyImpl();
  }

  public DiscountPolicy rateDiscountPolicyImpl() {
    return new RateDiscountPolicyImpl();
  }
}
