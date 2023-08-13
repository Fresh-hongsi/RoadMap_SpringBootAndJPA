package hello.core.dicount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component //type이 DiscountPolicy인 빈 (rate,fix)둘 다 컴포넌트 스캔 대상으로 하면 중복 문제 발생! 스프링 컨테이너는 타입으로 조회하는데 , 둘 다 TYPE이 dIScountPolicy이므로 문제다
//@Qualifier("fixDiscountPolicy") //의존관계 주입시, 같은 타입이 여러개면 중복 문제 발생하는 경우, 추가적인 구분자 역할을 함-> 더 세밀하게 가져올 컴포넌트 선택 가능
public class FixDiscountPolicy implements DiscountPolicy{ //할인 정책에 대한 구현체 1 -> vip대상 정액 할인

    private int discountFixAmount = 1000; //vip대상 정액할인 금액은 1000원임

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade()== Grade.VIP) //파라미터로 들어온 멤버가 vip 등급이라면
        {
            return discountFixAmount; //1000원이 할인됨을 return
        }
        else {
            return 0; //vip가 아니라면 0원 할인됨을 return
        }

    }
}
