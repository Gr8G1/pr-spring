package Gr8G1.prac.member;

public interface MemberRepository {
  void save(Member member);

  Member findById(Long memberId);
}
