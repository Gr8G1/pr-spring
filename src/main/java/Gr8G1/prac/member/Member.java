package Gr8G1.prac.member;

public class Member {
  private final Long id;
  private final String name;
  private final MemberGrade grade;

  public Member(Long id, String name, MemberGrade grade) {
    this.id = id;
    this.name = name;
    this.grade = grade;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public MemberGrade getGrade() {
    return grade;
  }
}
