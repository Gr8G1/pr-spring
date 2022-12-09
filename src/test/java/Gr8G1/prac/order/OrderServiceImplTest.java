package Gr8G1.prac.order;

import Gr8G1.prac.AppConfig;
import Gr8G1.prac.member.Member;
import Gr8G1.prac.member.MemberGrade;
import Gr8G1.prac.member.MemberRepository;
import Gr8G1.prac.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {
  AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
  MemberService memberService = ac.getBean("memberService", MemberService.class);
  OrderService orderService = ac.getBean("orderService", OrderService.class);

  @Test
  @DisplayName("VIP 할인 주문 테스트")
  public void createOrderWithDiscount() {
    Member vipMember = new Member(1L, "VIPMember", MemberGrade.VIP);
    Member basicMember = new Member(2L, "BasicMember", MemberGrade.Basic);
    memberService.join(vipMember);
    memberService.join(basicMember);

    Order orderVIP = orderService.createOrder(vipMember.getId(), "Apple", 20000);
    Order orderBasic = orderService.createOrder(basicMember.getId(), "Apple", 20000);
    // VIP 고정 할인 1000원 (FixedDiscountPolicyImpl)
    // assertThat(orderVIP.getDiscountPrice()).isEqualTo(1000);

    // VIP 비율 할인 10% (RateDiscountPolicyImpl)
    assertThat(orderVIP.getDiscountPrice()).isEqualTo(2000);

    // 일반 할인 X
    assertThat(orderBasic.getDiscountPrice()).isEqualTo(0);
  }
}
