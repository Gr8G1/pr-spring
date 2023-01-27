package Gr8G1.prac.auth.api.user.entity;

import Gr8G1.prac.auth.audit.Auditable;
import Gr8G1.prac.auth.oauth2.entity.ProviderType;
import Gr8G1.prac.auth.oauth2.entity.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userSeq;

  @Column(length = 64, nullable = false, unique = true)
  private String userId;

  @Column(length = 100, nullable = false)
  private String name;

  @JsonIgnore
  @Column(length = 128, nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @NotNull
  @Size(min = 1, max = 1)
  private String emailVerifiedYn;

  @Column(length = 13, unique = true)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private RoleType roleType;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ProviderType providerType;

  @NotNull
  @Column(length = 512)
  private String imageUrl;

  public User(String userId, String name, String email, String emailVerifiedYn, String phone, String imageUrl, ProviderType providerType, RoleType roleType) {
    this.userId = userId;
    this.name = name;
    this.password = "NO_PASS";
    this.email = email != null ? email : "NO_EMAIL";
    this.emailVerifiedYn = emailVerifiedYn;
    this.phone = phone != null ? phone : "";
    this.imageUrl = imageUrl != null ? imageUrl : "";
    this.providerType = providerType;
    this.roleType = roleType;
  }

  @Getter
  @AllArgsConstructor
  public enum MemberStatus {
    MEMBER_ACTIVE("활동중"),
    MEMBER_SLEEP("휴면 상태"),
    MEMBER_QUIT("탈퇴 상태");

    @Getter
    private final String status;
  }
}
