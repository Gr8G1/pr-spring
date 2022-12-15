package Gr8G1.prac.study.discount;

import Gr8G1.prac.study.member.Member;
import Gr8G1.prac.study.member.MemberGrade;

public class RateDiscountPolicyImpl implements DiscountPolicy {
  @Override
  public Integer discount(Member member, Integer price) {
    // 10% 할인
    Integer rateDiscount = 10;

    if (member.getGrade() == MemberGrade.VIP) {
      return price * rateDiscount / 100;
    }

    return 0;
  }
}
