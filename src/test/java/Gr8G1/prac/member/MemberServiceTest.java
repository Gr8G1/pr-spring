package Gr8G1.prac.member;

import Gr8G1.prac.AppConfig;
import Gr8G1.prac.order.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class MemberServiceTest {
  AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
  MemberService memberService = ac.getBean("memberService", MemberService.class);

  @Test
  @DisplayName("회원 생성/조회 테스트")
  public void join() {
    Member testMember = new Member(1L, "TestMember", MemberGrade.VIP);
    memberService.join(testMember);

    Member findMember = memberService.findMember(testMember.getId());

    assertThat(testMember).isEqualTo(findMember);
  }
}
