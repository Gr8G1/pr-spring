package Gr8G1.prac.discount;

import Gr8G1.prac.member.Member;
import Gr8G1.prac.member.MemberGrade;

public class FixedDiscountPolicyImpl implements DiscountPolicy {
  @Override
  public Integer discount(Member member, Integer price) {
    // 1000 정액 할인
    Integer discountFixAmount = 1000;

    if (member.getGrade() == MemberGrade.VIP) return discountFixAmount;

    return 0;
  }
}
