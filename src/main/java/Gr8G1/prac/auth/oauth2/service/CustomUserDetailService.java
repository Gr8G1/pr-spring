package Gr8G1.prac.auth.oauth2.service;

import Gr8G1.prac.auth.oauth2.entity.UserPrincipal;
import Gr8G1.prac.auth.api.user.entity.User;
import Gr8G1.prac.auth.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> byUserId = userRepository.findByUserId(username);

    User user = byUserId.orElseThrow(() -> new UsernameNotFoundException("Can not find username."));

    return UserPrincipal.create(user);
  }
}
