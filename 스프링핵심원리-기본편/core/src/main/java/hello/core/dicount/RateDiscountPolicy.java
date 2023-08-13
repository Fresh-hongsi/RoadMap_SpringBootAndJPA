package hello.core.dicount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary //이 어노테이션이 적혀있으면, 같은 타입이 여러개면 중복 문제 발생하는 경우, primary 적힌 컴포넌트가 우선순위를 갖고 빈에 등록된다
//@Qualifier("mainDiscountPolicy") //의존관계 주입시, 같은 타입이 여러개면 중복 문제 발생하는 경우, 추가적인 구분자 역할을 함-> 더 세밀하게 가져올 컴포넌트 선택 가능
@MainDiscountPolicy //내가 만든 어노테이션으로 퀄리파이러 동작시키기-> 컴파일 타임에 에러 잡을 수 있게 됨
public class RateDiscountPolicy implements DiscountPolicy { //할인 정책과 관련된 구현체

    private int discountPercent = 10; //할인 정률: 10%
    @Override
    public int discount(Member member, int price) {

        if(member.getGrade()== Grade.VIP) //회원이 vip라면
            return price*discountPercent/100; //할인할 금액 계산
        else { //vip가 아니라면
            return 0; //할인은 0원
        }
    }


}
