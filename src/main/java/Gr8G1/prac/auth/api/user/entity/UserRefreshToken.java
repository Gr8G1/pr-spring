package Gr8G1.prac.auth.api.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS_REFRESH_TOKEN")
public class UserRefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long refreshTokenSeq;

  @Column(length = 64, nullable = false, unique = true)
  private String userId;


  @Column(length = 256, nullable = false)
  private String refreshToken;

  public UserRefreshToken(String userId, String refreshToken) {
    this.userId = userId;
    this.refreshToken = refreshToken;
  }
}
