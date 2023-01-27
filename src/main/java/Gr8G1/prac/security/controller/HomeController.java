package Gr8G1.prac.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/login/oauth2")
@RequiredArgsConstructor
public class HomeController {
  private final OAuth2AuthorizedClientService authorizedClientService;

  // @GetMapping("/google")
  // public String google(Authentication authentication) {
  //   // ~ SecurityContextHolder
  //   Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  //   OAuth2User oAuth2User = (OAuth2User) principal1;
  //
  //   // ~ Authentication
  //   OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
  //
  //   log.info("OAuth2User get email={}", oAuth2User.getAttributes().get("email"));
  // }

  @GetMapping("/google")
  // public String oAuth2Google(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
  // ..
  // }
  public String oAuth2Google(@AuthenticationPrincipal OAuth2User oAuth2User) {

    OAuth2AuthorizedClient authorizedClient = authorizedClientService
                                      .loadAuthorizedClient("google", oAuth2User.getName());

    OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
    System.out.println("Access Token Value: " + accessToken.getTokenValue());
    System.out.println("Access Token Type: " + accessToken.getTokenType().getValue());
    System.out.println("Access Token Scopes: " + accessToken.getScopes());
    System.out.println("Access Token Issued At: " + accessToken.getIssuedAt());
    System.out.println("Access Token Expires At: " + accessToken.getExpiresAt());

    return "home";
  }
}
