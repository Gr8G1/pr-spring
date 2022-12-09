package Gr8G1.prac.discount;

import Gr8G1.prac.member.Member;
import Gr8G1.prac.member.MemberGrade;

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
