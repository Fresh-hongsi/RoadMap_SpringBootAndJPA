package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
public class PrototypeTest {

    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);//Prototype 클래스를 이런식으로 적으면 스프링 컴포넌트 스캔의 대상이 됨

        System.out.println("find PrototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class); //getBean하기 직전에 생성하고, 의존관계 주입까지 하고 반환, 그 후에 따로 관리x
        System.out.println("find PrototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class); //getBean하기 직전에 생성하고, 의존관계 주입까지 하고 반환, 그 후에 따로 관리x

        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);
        //서로 다른 객체가 나올 것

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);


        //prototype 스코프 쓰면 객체 만들고 반환하고 따로 스프링 컨테이너가 관리하지 않으므로 destroy 메서드(@PreDestroy)는 호출되지 않음을 기억하자!!!!
        //destroy를 하고 싶으면 클라이언트가 다음과 같은 방식으로 직접 호출해줘야함(수동으로)
        prototypeBean1.destroy();
        prototypeBean2.destroy();
        ac.close();

    }

    @Scope("prototype") //프로토타입 스코프 -> 요청 들어오면 생성, 의존관계 주입까지만 하고 반환, 그 후엔 스프링 컨테이너가 따로 관리하지 않음
    static class PrototypeBean{
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");

        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");

        }
    }
}
