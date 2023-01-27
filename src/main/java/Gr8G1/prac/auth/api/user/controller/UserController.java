package Gr8G1.prac.auth.api.user.controller;

import Gr8G1.prac.auth.api.user.entity.User;
import Gr8G1.prac.auth.api.user.mapper.UserMapper;
import Gr8G1.prac.auth.api.user.service.UserService;
import Gr8G1.prac.auth.response.ApiResponse;
import Gr8G1.prac.auth.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Validated
public class UserController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<?> getUser() {
    org.springframework.security.core.userdetails.User principal =
      (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    User user = userService.findUser(principal.getUsername());

    return ResponseEntity.ok().body(ApiResponse.ok("data", user));
  }
}
