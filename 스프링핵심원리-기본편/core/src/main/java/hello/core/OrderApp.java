package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.Order;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp { //주문과 관련된 main함수 기반 테스트 코드
    public static void main(String[] args) {


        //AppConfig appConfig = new AppConfig();

        //MemberService memberService = new MemberServiceImpl(null); //회원 서비스 가져오기
       // MemberService memberService = appConfig.memberService(); //이제 AppConfig로 memberServiceImpl을 받아옴

        //OrderService orderService =  new OrderServiceImpl(null,null); //주문 서비스 가져오기
       // OrderService orderService = appConfig.orderService(); //이제 AppConfig로 OrderServiceImpl을 받아옴

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);//AppConfig의 @Bean이 붙은 "memberService"라는 이름의 메서드고, 타입이 MemberService인 걸 가져와
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);//AppConfig의  @Bean이 붙은 "orderService"라는 이름의 메서드고, 타입이 OrderService인 걸 가져와


        Long memberId= 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP); //vip 회원 생성
        memberService.join(member); //회원 레포지토리에 회원 저장

        Order order = orderService.createOrder(memberId, "itemA", 20000);//주문하기, 주문 결과 반환
        System.out.println("order = " + order); //주문 결과 출력
        //System.out.println("order.calculatePrice() = " + order.calculatePrice());; //주문 완료된 주문 기반으로 만들어놓은 계산 메서드 호출해서 할인 적용 금액 반환
    }
}
