package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
public class SingletonWithPrototypeTest1
{

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class); //getBean하기 직전에 생성하고, 의존관계 주입까지 하고 반환, 그 후에 따로 관리x
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class); //getBean하기 직전에 생성하고, 의존관계 주입까지 하고 반환, 그 후에 따로 관리x
        prototypeBean2.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class,ClientBean.class); //싱글톤, 프로토타입 둘다 넣어줌

        ClientBean clientBean1 = ac.getBean(ClientBean.class); //클라이언트 a가 싱글톤 객체 받아옴
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class); //클라이언트 b가 싱글톤 객체 받아옴
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);

    }

    @Scope("singleton")
    static class ClientBean {

        //private final PrototypeBean prototypeBean; //이 싱글톤 클래스 안에서 프로토타입빈을 의존관계 주입을 통해 받을 것, 즉 생성시점에 주입됨

        @Autowired private Provider<PrototypeBean> prototypeBeanProvider; //이 방식 쓰면 PrototypeBean 객체를 새로 계속 만들어서 넣어줌


//        //생성자가 한개이면 오토와이어드 생략 가능
//        @Autowired
//        public ClientBean(PrototypeBean prototypeBean) {
//            this.prototypeBean = prototypeBean;
//        }

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.get(); //logic 호출때마다 프로토타입 스코프의 객체가 만들어서 반환됨
            prototypeBean.addCount(); //외부에서 이 싱글톤 객체에 접근하고, 이 싱글톤이 자기의 애트리뷰트로 갖고 있는 프로토타입 스코프의 객체의 addcount호출
            //싱글톤 안에 있는 프로토타입은 이미 딱 한번 생성되고, 이 싱글톤에 의존관계가 맺혀졌으므로 사라지지 않고 딱 한개의 인스턴스가 계속 보관되어있다
            //따라서 logic호출하면 계속 하나의 프로토타입 인스턴스에 addcount가 되므로 1->2->3..이 됨

            return prototypeBean.getCount();
        }
    }
    @Scope("prototype")
    static class PrototypeBean {
        private int count=0;

        public void addCount(){
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init "+this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
