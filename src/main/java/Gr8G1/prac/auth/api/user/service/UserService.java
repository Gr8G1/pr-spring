package Gr8G1.prac.auth.api.user.service;

import Gr8G1.prac.auth.exception.BusinessException;
import Gr8G1.prac.auth.exception.ExceptionCode;
import Gr8G1.prac.auth.api.user.entity.User;
import Gr8G1.prac.auth.api.user.repository.UserRepository;
import Gr8G1.prac.auth.oauth2.entity.ProviderType;
import Gr8G1.prac.auth.oauth2.entity.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User createUser(User user) {
    verifyExistsUserId(user.getUserId());

    String encryptedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encryptedPassword);

    user.setImageUrl("");
    user.setEmailVerifiedYn("N");
    user.setRoleType(RoleType.USER);
    user.setProviderType(ProviderType.LOCAL);

    return userRepository.save(user);
  }

  public User findUser(String userId) {
    return findVerifiedUser(userId);
  }

  @Transactional(readOnly = true)
  public User findVerifiedUser(String userId) {
    Optional<User> optionalMember = userRepository.findByUserId(userId);

    return optionalMember.orElseThrow(() -> new BusinessException(ExceptionCode.MEMBER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public void verifyExistsUserId(String userId) {
    userRepository.findByUserId(userId).ifPresent((e) -> {
      throw new BusinessException(ExceptionCode.MEMBER_EXISTS);
    });
  }
}
