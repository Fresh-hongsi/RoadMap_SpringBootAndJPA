package hello.core.dicount;


import hello.core.member.Member;

public interface DiscountPolicy { //할인 정책과 관련된 인터페이스


    /**
     * @return: 할인 대상 금액
     */
    int discount(Member member, int price); //
}
