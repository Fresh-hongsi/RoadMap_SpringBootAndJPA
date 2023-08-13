package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.annotation.Documented;

import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig= new AppConfig();

        //1. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();

        //2. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        //3. 참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //memberService != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest(){
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        //같은 인스턴스가 반환됨
        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);

        assertThat(singletonService1).isSameAs(singletonService2);
        //isSameAs: 자바 ==과 동일 -> 주소의 값을 비교합니다.(Call By Reference)
        //isEqualTo: 자바 equals랑 동일 -> 객체끼리 내용 비교합니다.(Call By value)
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {

        //AppConfig appConfig= new AppConfig();
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        //1. 조회: 호출할 때 마다 등록된 애를 가져옴
        //MemberService memberService1 = appConfig.memberService();
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);

        //2. 조회: 호출할 때 마다 등록된 애를 가져옴
        //MemberService memberService2 = appConfig.memberService();
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        //3. 참조값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        //memberService != memberService2
        assertThat(memberService1).isSameAs(memberService2);
    }
}
