package Gr8G1.prac.auth.oauth2.handler;

import Gr8G1.prac.auth.response.ErrorResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException exception
  ) throws IOException {
    log.error("Authentication failed = {}", exception.getMessage());

    sendErrorResponse(response);
  }

  private void sendErrorResponse(HttpServletResponse response) throws IOException {
    ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED);
    Gson gson = new Gson();

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter()
      .write(gson.toJson(errorResponse, ErrorResponse.class));
  }
}
