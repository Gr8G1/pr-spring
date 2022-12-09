package Gr8G1.prac.member;

import Gr8G1.prac.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberServiceTest {
  @Test
  @DisplayName("회원 생성/조회 테스트")
  public void join() {
    AppConfig appConfig = new AppConfig();
    MemberRepository memberRepository = appConfig.memberRepository();
    MemberService memberService = appConfig.memberService(memberRepository);

    Member testMember = new Member(1L, "TestMember", MemberGrade.VIP);
    memberService.join(testMember);

    Member findMember = memberService.findMember(testMember.getId());

    assertThat(testMember).isEqualTo(findMember);
  }
}
