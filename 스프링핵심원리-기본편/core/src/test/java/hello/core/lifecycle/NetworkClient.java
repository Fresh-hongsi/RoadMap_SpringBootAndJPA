package hello.core.lifecycle;

//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient  {
    //가짜 네트워크 클라이언트
    //InitializingBean : 초기화 빈
    private String url; //접속해야할 서버의 url

    public NetworkClient() { //디폴트 생성자
        System.out.println("생성자 호출, url = "+url);
        //connect();
        //call("초기화 연결 메시지");
    }

    //url은 외부에서 setter로 넣어줄 수 있게 세팅
    public void setUrl(String url) {
        this.url = url;
    }

    //서비스를 시작할 떄 호출
    public void connect() {
        System.out.println("connect: "+url);
    }

    //실제 연결 후에 하는 call관련
    public void call(String message)
    {
        System.out.println("call: "+url+" message = "+message);
    }

    //서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: "+url);
    }


    ///////////////////////////////의존관계 주입이 끝나면 호출해주겠다!는 InitializingBean 인터페이스의 메서드
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        //connect랑 call를 여기에 추가
//        System.out.println("NetworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    ///////////////////////////////DisposableBean 인터페이스의 메서드, 빈이 소멸되기 직전에 호출된다
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destroy");
//        disconnect();
//    }


    @PostConstruct //결과적으로 3가지 방식 중에 이 방식을 쓴다(애노테이션 방식 - 초기화 콜백, 초기화 완료된 시점에 호출)
    public void init()  {
        //connect랑 call를 여기에 추가
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy //결과적으로 3가지 방식 중에 이 방식을 쓴다(애노테이션 방식 - 소멸전 콜백)
    public void close()  {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
