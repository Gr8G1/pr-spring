package Gr8G1.prac.study.discount;

import Gr8G1.prac.study.member.Member;

public interface DiscountPolicy {
  public Integer discount(Member member, Integer price);
}
