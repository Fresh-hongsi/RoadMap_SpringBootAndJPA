package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
public class SingletonTest {

    @Test
    void singletonBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);//SingletonBean 클래스를 이런식으로 적으면 스프링 컴포넌트 스캔의 대상이 됨

        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);//스프링 컨테이너에 등록되어있는 싱글톤 스코프의 빈을 가져옴
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);//스프링 컨테이너에 등록되어있는 싱글톤 스코프의 빈을 가져옴
        System.out.println("singletonBean1 = " + singletonBean1); //싱글톤 스코프이므로 같은 빈이 조회됨
        System.out.println("singletonBean2 = " + singletonBean2); //싱글톤 스코프이므로 같은 빈이 조회됨

        assertThat(singletonBean1).isSameAs(singletonBean2);

        ac.close(); //이거 호출하면 destory메서드 호출
    }

    @Scope("singleton") //싱글톤 스코프
    static class SingletonBean{

        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");

        }

        @PreDestroy
        public void destroy(){
            System.out.println("SingletonBean.destroy");
        }
    }
}
