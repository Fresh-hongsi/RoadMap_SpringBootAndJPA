package hello.core;

import hello.core.dicount.DiscountPolicy;
import hello.core.dicount.FixDiscountPolicy;
import hello.core.dicount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.Order;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//이런 방식으로 빈 등록하는 걸 factory method 방식이라고 함 (팩토리 빈을 등록하는 방식)
//스프링 빈으로 등록해두면 싱글톤으로 관리됨
@Configuration //스프링을 활용한 configuration을 위해 필요한 어노테이션
public class AppConfig //애플리케이션의 전체 동작 방식을 구성(config)하기 위해, 구현 객체를 생성하고, 연결하는 책임을 가지는 별도의 설정 클래스
    //즉 AppConfig는 애플리케이션의 실제 동작에 필요한 구현객체를 생성한다
    //AppConfig는 생성한 객체 인스턴스의 참조(레퍼런스)를 생성자를 통해서 주입(연결)해준다
{

    //각 메서드에 @Bean이라고 적는 이유: @BEAN한 거가 스프링 컨테이너에 등록됨
    @Bean
    //리팩터링-> 각 함수가 뭘 하려고 하는지 명확하게 구분함
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService(){
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
        //return null;
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy(); //정률할인 정책으로 변경 위해 이 코드만 바꾸면 됨
    }
}
