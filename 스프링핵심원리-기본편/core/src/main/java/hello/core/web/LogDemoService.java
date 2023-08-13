package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    //private final ObjectProvider<MyLogger> myLoggerProvider; //provider 통해 사용 시점에 딱 객체 생성해서 넣어줌
    private final MyLogger myLogger; //프록시 방식
    public void logic(String id) {

        //MyLogger myLogger = myLoggerProvider.getObject(); //필요한 시점에 딱 받아옴
        myLogger.log("service id = "+id);


    }
}
