package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.dicount.DiscountPolicy;
//import hello.core.dicount.FixDiscountPolicy;
//import hello.core.dicount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor //롬복 라이브러리, final이 붙은 필드 값을 기반으로 한 생성자를 자동으로 만들어줌(겉으로는 코드로 안드러남, ctrl+f12눌러보면 생성자가 들어와있음을 알 수 있음)
public class OrderServiceImpl implements OrderService{ //OrderService 인터페이스에 대한 구현체


    //주문 서비스는 주문을 만들고, 회원 레포지토리에 접근해서 회원을 받아와서, 해당 회원이 vip라면 할인금액을 알아오고,
    //그 다음 최종 주문 결과 객체 Order를 만들어서 반환해야함

    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy(); // 할인 정책 변경

    //@Autowired(required = false) //autowired가 빠져있으면 setter 주입 불가 -> 선택적으로 의존관계 주입가능(required = false) 조건 필요
    //setter주입같은 경우 아래의 memberRepository가 스프링 빈에 등록이 되어있지 않아도사용 가능하다!!!! -> 선택적으로 의존관계 주입가능(@AUTOWIRED는 주입 대상이 없으면 오류 발생하므로 required=false란 조건을 넣어줘야 메모리멤버리포가 없어도 동작한다)
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) { //수정자 주입 관련 -> autowired가 되어있으므로 spring실해시 sout통해 잘 들어왔음을 알 수 잇음
//        //System.out.println("memberRepository = " + memberRepository);
//        this.memberRepository = memberRepository;
//    }
////
//   @Autowired //autowired가 빠져있으면 Setter 주입 불가
////    //setter주입같은 경우 아래의 discountPolicy가 스프링 빈에 등록이 되어있지 않아도사용 가능하다!!!! -> 선택적으로 의존관계 주입가능
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) { //수정자 주입 관련 -> autowired가 되어있으므로 spring실해시 sout통해 잘 들어왔음을 알 수 잇음
//        //System.out.println("discountPolicy = " + discountPolicy);
//        this.discountPolicy = discountPolicy;
//    }

    //위의 세 줄을 아래 두 줄로 변경, final 키워드가 있으면 생성자든 직접할당이든 무조건 할당이 되어야함
    //필드주입 방법 -> 사용하지 않는게 좋다
    //생성자 주입 사용시 final 키워드 삽입 가능(다른 주입 방식에서는 final 사용 불가)
    private final MemberRepository memberRepository; //dip, ocp 원칙을 지키기 위해 이렇게 변경
    private  final DiscountPolicy discountPolicy; //dip, ocp 원칙을 지키기 위해 이렇게 변경

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //@Autowired 는 타입 매칭을 시도하고, 이때 여러 빈이 있으면 필드 이름, 파라미터 이름으로 빈 이름을 추가
            //매칭한다
    //!!!!!!!!!!!!!!!!!!!!!!!!
    //appconfig에서 orderServiceImpl에 관련된 의존성 있는 것들을 주입해줄 것

    //setter주입을 해줬으므로 생성자가 필요없어짐-> setter주입하면 위의 변수 두개 final생략 가능
    @Autowired //생성자가 딱 1개만 있으면 @Autowired를 생략해도 자동 주입된다
    public OrderServiceImpl(MemberRepository memberRepository,  @MainDiscountPolicy DiscountPolicy discountPolicy) { //DiscountPolicy타입은 두개인데, 퀄리파이어 통해 rateDiscountPolicy를 가져옴
        //System.out.println("memberRepository = " + memberRepository);
        //System.out.println("discountPolicy = " + discountPolicy);
        //System.out.println("1. OrderServiceImpl.OrderServiceImpl");
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

//    @Autowired
//    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy)
//    {
//        this.memberRepository=memberRepository;
//        this.discountPolicy=discountPolicy;
//    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) //주문과 관련된 함수 호출하면
    {
        Member member = memberRepository.findById(memberId); //회원 레포지토리에서 회원을 찾고
        int discountPrice = discountPolicy.discount(member, itemPrice); //받아온 회원 정보로 할인 금액 알아옴

        return new Order(memberId,itemName, itemPrice, discountPrice);

    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
