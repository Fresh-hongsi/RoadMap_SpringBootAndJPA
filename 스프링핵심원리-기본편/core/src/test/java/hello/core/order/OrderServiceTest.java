package hello.core.order;

import hello.core.AppConfig;
import hello.core.dicount.FixDiscountPolicy;
import hello.core.member.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
public class OrderServiceTest {

    //MemberService memberService=new MemberServiceImpl();
    //OrderService orderService =  new OrderServiceImpl();

    MemberService memberService;
    OrderService orderService;

    @BeforeEach //각 테스트 전에 무조건 실행
    public void beforeEach()
    {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    void createOrder() {
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        Order order = orderService.createOrder(memberId, "itemA", 10000);

        assertThat(order.getDiscountPrice()).isEqualTo(1000); //assertj로 할인금액 동적 테스트
    }



}
