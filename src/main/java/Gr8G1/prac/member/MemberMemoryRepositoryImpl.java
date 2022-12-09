package Gr8G1.prac.member;

import java.util.HashMap;

public class MemberMemoryRepositoryImpl implements MemberRepository {
  private final HashMap<Long, Member> store = new HashMap<>();

  @Override
  public void save(Member member) {
    store.put(member.getId(), member);
  }

  @Override
  public Member findById(Long memberId) {
    return store.get(memberId);
  }
}
