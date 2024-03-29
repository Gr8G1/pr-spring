package Gr8G1.prac.auth.oauth2.service;

import Gr8G1.prac.auth.exception.OAuthProviderMissMatchException;
import Gr8G1.prac.auth.oauth2.entity.RoleType;
import Gr8G1.prac.auth.oauth2.entity.UserPrincipal;
import Gr8G1.prac.auth.api.user.entity.User;
import Gr8G1.prac.auth.api.user.repository.UserRepository;
import Gr8G1.prac.auth.oauth2.info.OAuth2UserInfo;
import Gr8G1.prac.auth.oauth2.entity.ProviderType;
import Gr8G1.prac.auth.oauth2.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User user = super.loadUser(userRequest);

    try {
      return this.process(userRequest, user);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
    ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
    Optional<User> byEmail = userRepository.findByEmail(userInfo.getEmail());
    User savedUser = byEmail.orElse(null);

    if (savedUser != null) {
      if (providerType != savedUser.getProviderType()) {
        throw new OAuthProviderMissMatchException(
          "Looks like you're signed up with " + providerType +
            " account. Please use your " + savedUser.getProviderType() + " account to login."
        );
      }
      updateUser(savedUser, userInfo);
    } else {
      savedUser = createUser(userInfo, providerType);
    }

    return UserPrincipal.create(savedUser, user.getAttributes());
  }

  private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
    User user = new User(
      userInfo.getId(),
      userInfo.getName(),
      userInfo.getEmail(),
      "Y",
      null,
      userInfo.getImageUrl(),
      providerType,
      RoleType.USER
    );

    return userRepository.saveAndFlush(user);
  }

  private User updateUser(User user, OAuth2UserInfo userInfo) {
    if (userInfo.getName() != null && !user.getName().equals(userInfo.getName())) {
      user.setName(userInfo.getName());
    }

    if (userInfo.getImageUrl() != null && !user.getImageUrl().equals(userInfo.getImageUrl())) {
      user.setImageUrl(userInfo.getImageUrl());
    }

    return user;
  }
}
