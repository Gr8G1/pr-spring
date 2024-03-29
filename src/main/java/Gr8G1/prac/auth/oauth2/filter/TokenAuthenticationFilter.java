package Gr8G1.prac.auth.oauth2.filter;

import Gr8G1.prac.auth.oauth2.token.AuthToken;
import Gr8G1.prac.auth.oauth2.token.AuthTokenProvider;
import Gr8G1.prac.auth.utils.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
  private final AuthTokenProvider authTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String tokenStr = HeaderUtil.getAccessToken(request);
    AuthToken token = authTokenProvider.convertAuthToken(tokenStr);

    if (token.validate()) {
      Authentication authentication = authTokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String authorization = request.getHeader("Authorization");

    return authorization == null || !authorization.startsWith("Bearer");
  }
}
