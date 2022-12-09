package Gr8G1.prac.discount;

import Gr8G1.prac.member.Member;

public interface DiscountPolicy {
  public Integer discount(Member member, Integer price);
}
