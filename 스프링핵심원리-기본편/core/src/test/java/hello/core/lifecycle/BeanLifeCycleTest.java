package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();  //ConfigurableApplicationContext 타입이어야 close메서드 호출 가능함
    }

    @Configuration
    static class LifeCycleConfig{
        //@Bean(initMethod = "init", destroyMethod = "close") //수동으로 NetworkClient객체를 스프링 컨테이너에 등록, 초기화, 소멸 메서드를 지정할 수 있다
        @Bean
        public NetworkClient networkClient(){
            NetworkClient networkClient = new NetworkClient(); //생성자 호출
            networkClient.setUrl("http://hello-spring.dev"); //받아온 객체에 url 세팅
            return networkClient;
        }
    }
}
