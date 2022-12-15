package Gr8G1.prac.study.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/*
 * # 스프링 빈의 이벤트 라이프사이클
 *  스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 소멸 전 콜백 -> 스프링 종료
 *  > 초기화 콜백: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
 *  > 소멸전 콜백: 빈이 소멸되기 직전에 호출
 *
 * # 스프링 빈 생명주기 콜백 3가지
 *  1. 인터페이스 (InitializingBean, DisposableBean)
 *    - 단점
 *      1. 해당 코드가 스프링 전용 인터페이스에 의존한다.
 *      2. 초기화, 소멸 메서드의 이름을 변경할 수 없다.
 *      3. 직접 코드를 고칠 수 없는 외부 라이브러리 사용시 인터페이스 콜백을 적용할 수 없다.
 *  2. @Bean 초기화, 종료 메서드 지정
 *  3. @PostConstruct, @PreDestroy 애노테이션 지원
 *
 */
public class NetworkClient { // implements InitializingBean, DisposableBean {
  String url;

  public NetworkClient() {
    System.out.println("생성자 호출::url=" + url);
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @PostConstruct
  void connect() {
    System.out.println("connect::url=" + url);
    call("네트워크 콜");
  }

  void call(String message) {
    System.out.println("call::url=" + url + " \n message: " + message);
  }

  @PreDestroy
  void disconnect() {
    System.out.println("disconnect::url=" + url);
  }

  // @Override
  // public void afterPropertiesSet() throws Exception {
  //   System.out.println("NetworkClient.afterPropertiesSet");
  //   connect();
  //   call("네트워크 콜");
  // }
  //
  // @Override
  // public void destroy() throws Exception {
  //   System.out.println("NetworkClient.destroy");
  //   disconnect();
  // }
}
