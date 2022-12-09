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
