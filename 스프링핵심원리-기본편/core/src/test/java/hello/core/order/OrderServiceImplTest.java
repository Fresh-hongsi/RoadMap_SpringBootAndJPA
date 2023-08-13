package hello.core.order;

import hello.core.dicount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemoryMemberRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
class OrderServiceImplTest {

    @Test
    void createOrder() {

        MemoryMemberRepository memberRepository=new MemoryMemberRepository();
        memberRepository.save(new Member(1L,"name", Grade.VIP));
        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository,new FixDiscountPolicy());

        //수정자 주입 방식이므로 orderService의 비어있는 객체 생성하면 안에 데이터에 아무 데이터도 세팅이 안되어있으므로 nullpointer exceeption나온다
        Order order = orderService.createOrder(1L,"item1",10000);
        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}