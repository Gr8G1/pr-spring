package Gr8G1.prac.study.member;

public interface MemberService {
  void join(Member member);
  Member findMember(Long memberId);
}
