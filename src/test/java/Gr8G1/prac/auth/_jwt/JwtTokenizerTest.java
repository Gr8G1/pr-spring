package Gr8G1.prac.auth._jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenizerTest {
  private static JwtTokenizer jwtTokenizer;
  private String secretKey;
  private String base64EncodedSecretKey;

  @BeforeAll
  public void init() {
    jwtTokenizer = new JwtTokenizer();
    secretKey = "JwtSecretKey1234567890abcdefghijklmnopqrstuvwxyz";
    base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);
  }

  @DisplayName("Base64EncodedSecretKey Decode 후 확인 테스트")
  @Test
  public void encodeBase64SecretKeyTest() {
    System.out.println("base64EncodedSecretKey = " + base64EncodedSecretKey);

    assertThat(secretKey).isEqualTo(new String(Decoders.BASE64.decode(base64EncodedSecretKey)));
  }

  @DisplayName("엑세스 토큰 생성 테스트")
  @Test
  public void generateAccessTokenTest() {
    Map<String, Object> claims = new HashMap<>() {{
      put("memberId", 1);
      put("roles", List.of("USER"));
    }};

    String subject = "JWT Access Token Test";

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, 10);

    Date expiration = calendar.getTime();

    String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    System.out.println("accessToken = " + accessToken);

    assertThat(accessToken).isNotNull();
  }

  @DisplayName("리프레시 토큰 생성 테스트")
  @Test
  public void generateRefreshTokenTest() {
    String subject = "test refresh token";
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, 24);
    Date expiration = calendar.getTime();

    String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
    System.out.println("refreshToken = " + refreshToken);

    assertThat(refreshToken).isNotNull();
  }

  @DisplayName("토큰 정보 증명 확인 성공시 예외 미발생 테스트")
  @Test
  public void verifySignatureTest() {
    String accessToken = getAccessToken(Calendar.MINUTE, 10);

    assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
  }

  @DisplayName("토큰 정보 증명 확인 실패시 예외 (ExpiredJwtException) 발생 테스트")
  @Test
  public void verifyExpirationTest() throws InterruptedException {
    String accessToken = getAccessToken(Calendar.SECOND, 2);
    assertDoesNotThrow(() -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));

    TimeUnit.MILLISECONDS.sleep(2000);

    assertThrows(ExpiredJwtException.class, () -> jwtTokenizer.verifySignature(accessToken, base64EncodedSecretKey));
  }

  private String getAccessToken(int timeUnit, int timeAmount) {
    Map<String, Object> claims = new HashMap<>() {{
      put("memberId", 1);
      put("roles", List.of("USER"));
    }};

    String subject = "JWT Access Token Test";

    Calendar calendar = Calendar.getInstance();
    calendar.add(timeUnit, timeAmount);

    Date expiration = calendar.getTime();

    return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
  }
}
