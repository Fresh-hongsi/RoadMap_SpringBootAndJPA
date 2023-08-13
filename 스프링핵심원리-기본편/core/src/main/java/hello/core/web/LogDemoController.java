package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor //롬복통해 생성자 대체
public class LogDemoController {

    private final LogDemoService logDemoService; //롬복 생성자 통해 의존관계 자동주입
    //private final ObjectProvider<MyLogger> myLoggerProvider; //provider 통해 사용 시점에 딱 객체 생성해서 넣어줌
    private final MyLogger myLogger; //프록시 방식

//    ObjectProvider 덕분에 ObjectProvider.getObject() 를 호출하는 시점까지 request scope 빈의
//    생성을 지연할 수 있다.
//            ObjectProvider.getObject() 를 호출하시는 시점에는 HTTP 요청이 진행중이므로 request scope
//    빈의 생성이 정상 처리된다.
//            ObjectProvider.getObject() 를 LogDemoController , LogDemoService 에서 각각 한번씩 따로 호
//    출해도 같은 HTTP 요청이면 같은 스프링 빈이 반환된다! 내가 직접 이걸 구분하려면 얼마나 힘들까 ㅠ
//    ㅠ…

    @RequestMapping("log-demo")
    @ResponseBody //문자로 바로 response해서 내보냄
    public String logDemo(HttpServletRequest request)throws InterruptedException{ //HttpServletRequest: 고객의 요청 정보를 받을 수 있는 자바 객체


        String requestURL = request.getRequestURL().toString(); //고객이 어떤 url로 요청했는지 알아냄
        //MyLogger myLogger = myLoggerProvider.getObject(); //필요한 시점에 딱 받아옴

        System.out.println("myLogger = " + myLogger.getClass());
        myLogger.setRequestURL(requestURL); //클라이언트에 해당하는 request객체에 고객의 url을 세팅

        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");
        return "OK";
    }


}
