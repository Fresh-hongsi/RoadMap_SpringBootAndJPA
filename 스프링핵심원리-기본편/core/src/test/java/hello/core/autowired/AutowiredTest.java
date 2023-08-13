package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class); //이렇게 testbean을 넣어주면 testbean이 자동으로 스프링 빈으로 등록됨


    }

    static class TestBean {

        //required = true면 컴파일 에러 뜸 - > 기대했는데 member가 bean으로 등록이 안되어있기 떄문에!
        //required = false이면 실행은 되나 setNoBean1 메서드가 호출 안됨
        @Autowired(required = false) //(required = false)라고 적었는데, 의존관계가 없으면 이 메서드(setNoBean1) 호출이 아예 안됨
        public void setNoBean1(Member noBean1) //여기서 Member은 spring bean이 관리하는 멤버가 아님
        {
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired //호출은 되는데, null이면 예외 안터지고 null값을 return
        public void setNoBean2(@Nullable Member noBean2) //여기서 Member은 spring bean이 관리하는 멤버가 아님
        {
            System.out.println("noBean2 = " + noBean2);
        }

        @Autowired //null이면 optional.empty라는 값을 return
        public void setNoBean3(Optional<Member> noBean3){ //여기서 Member은 spring bean이 관리하는 멤버가 아님
            System.out.println("noBean3 = " + noBean3);
        }
    }
}
