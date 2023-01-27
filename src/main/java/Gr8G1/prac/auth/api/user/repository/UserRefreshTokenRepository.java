package Gr8G1.prac.auth.api.user.repository;

import Gr8G1.prac.auth.api.user.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
  UserRefreshToken findByUserId(String userId);
  UserRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}
