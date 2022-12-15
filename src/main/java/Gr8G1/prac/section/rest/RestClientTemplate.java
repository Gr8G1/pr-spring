package Gr8G1.prac.section.rest;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class RestClientTemplate {
  public static void main(String[] args) {
    RestTemplate restTemplate =  new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    UriComponents uriComponents = UriComponentsBuilder
        .newInstance()
        .scheme("http")
        .host("worldtimeapi.org")
        .port(80)
        .path("/api/timezone/{continents}/{city}")
        .encode()
        .build();

    URI uri = uriComponents.expand("Asia", "Seoul").toUri();
    System.out.println("uri = " + uri);

    // 단순 오브젝트 방식
    // String result = restTemplate.getForObject(uri, String.class);
    // System.out.println("result = " + result);

    // 클래스 지정 방식
    // WorldTime worldTimeResult = restTemplate.getForObject(uri, WorldTime.class);
    // System.out.println("result = " + worldTimeResult);

    // ResponseEntity 방식
    // ResponseEntity<WorldTime> responseEntity = restTemplate.getForEntity(uri, WorldTime.class);
    // System.out.println("responseEntity = " + responseEntity);

    // ResponseEntity Exchange 지정 방식
    ResponseEntity<WorldTime> responseExchange = restTemplate.exchange(uri, HttpMethod.GET,null, WorldTime.class);
    System.out.println("responseExchange = " + responseExchange);
  }

  /*
   * # UriComponentsBuilder API 해설
   * newInstance()
   *  - UriComponentsBuilder 객체를 생성
   * scheme()
   *  - URI의 scheme을 설정
   * host()
   *  - 호스트 정보를 입력
   * port()
   *  - 디폴트 값은 80이므로 80 포트를 사용하는 호스트라면 생략 가능
   * path()
   *  - URI의 경로(path)를 입력
   *  - *URI 템플릿 패턴* 적용
   * encode()
   *  - URI에 사용된 템플릿 변수 인코딩
   * build()
   *  - UriComponents 객체를 생성
   *
   * uriComponents.expand
   *  - URI 템플릿 패턴 변수에 값 할당
   * toUri
   *  - URI 생성
   *
   */

  /*
   * # REST Cilents DOC
   *  - https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#rest-client-access
   *
   */
}

class WorldTime {
  private String datetime;
  private String timezone;
  private int day_of_week;

  public String getDatetime() {
    return datetime;
  }

  public String getTimezone() {
    return timezone;
  }

  public int getDay_of_week() {
    return day_of_week;
  }
}
