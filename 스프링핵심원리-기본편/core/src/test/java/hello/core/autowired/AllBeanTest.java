package hello.core.autowired;

import hello.core.AutoAppConfig;
import hello.core.dicount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
import java.util.List;
import java.util.Map;

public class AllBeanTest {

    @Test
    void findAllBean()
    {
        //AutoAppConfig에서 DiscountPolicy끌어오기
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class,DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class); //등록된 disCountService 객체 받아오고
        Member member = new Member(1L, "userA", Grade.VIP); //멤버를 하나 생성
        int discountPrice = discountService.discount(member,10000,"fixDiscountPolicy"); //할인 정책 수행

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        int rateDiscountPrice = discountService.discount(member,20000,"rateDiscountPolicy"); //할인 정책 수행
        assertThat(rateDiscountPrice).isEqualTo(2000);
    }

    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap; //map에 모든 할인 정책 받아와 저장(fix,rate)
        private final List<DiscountPolicy> policies; //list에 모든 할인 정책 받아와 저장

        @Autowired
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        public int discount(Member member, int price, String discountCode){

            DiscountPolicy discountPolicy = policyMap.get(discountCode);//할인 정책이 뭔지 확인해서 해당 할인 정책 인스턴스 가져오기 ->fixdiscountPoicy
            return discountPolicy.discount(member,price);
        }
    }


}
