package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //ThreadA: A사용자가 10000원 주문
        int priceUserA = statefulService1.order("userA",10000);
        //ThreadB: B사용자가 20000원 주문
        int priceUserB =statefulService2.order("userB",20000);

        //ThreadA: 사용자A가 주문 금액 조회
        //즉 a가 주문하고 금액 조회하는 사이에 b가 끼어들었음
        //int price = statefulService1.getPrice();
        System.out.println("price = " + priceUserA); //20000원이 나오게 됨 ->예상값: 10000원임에도 불구하고! -> 수정 후 정상작동하게 됨

        //assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    //statefulService 전용 config 클래스
    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}