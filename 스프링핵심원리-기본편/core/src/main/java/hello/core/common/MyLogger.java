package hello.core.common;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
@Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS) //request scope로 등록 -> 이 빈은 http요청 당 하나씩 생성됨, 프록시 방식으로 동작하게 설정함
//여기가 핵심이다. proxyMode = ScopedProxyMode.TARGET_CLASS 를 추가해주자.
//        적용 대상이 인터페이스가 아닌 클래스면 TARGET_CLASS 를 선택
//        적용 대상이 인터페이스면 INTERFACES 를 선택
//        이렇게 하면 MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클
//        래스를 다른 빈에 미리 주입해 둘 수 있다
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) { //requestUrl은 중간에 세팅하도록 구성함
        this.requestURL = requestURL;
    }

    public void log(String message){
        System.out.println("["+uuid+"]"+"["+requestURL+"] "+message);
    }

    @PostConstruct //생성 후 초기화(의존관계 주입)까지 된 후에 호출됨
    public void init(){
        uuid = UUID.randomUUID().toString(); //전 글로벌적으로 유니크한 아이디를 하나 할당받음
        System.out.println("["+uuid+"] request scope bean create: "+this); //this통해 이 request 스코프 객체도 알려줌
    }

    @PreDestroy // request 스코프 쓰면, 프로토타입 스코프와 달리 스프링에서 소멸까지 관리해줌
    public void close() {
        System.out.println("["+uuid+"] request scope bean close: "+this); //this통해 이 request 스코프 객체도 알려줌
    }


}
