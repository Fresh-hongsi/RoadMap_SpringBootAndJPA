package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {

        //AppConfig appConfig = new AppConfig();

        //MemberService memberService = new MemberServiceImpl(); //다형성 통해 memberService로 실제 구현된 MemberServiceImpl의 메서드 호출!
        //MemberService memberService = appConfig.memberService(); //이제 AppConfig로 memberServiceImpl을 받아옴

        //스프링은 모든게 AppicationContext라는 걸로 시작함. 즉 이게 스프링 컨테이너임. 따라서 모든 빈 관리
        //ApplicationContext는 스프링 컨테이너이자 인터페이스이다
        //AnnotationConfigApplicationContext가 구현체이다
        //아래와 같이 적으면 AppConfig에 있는 환경설정 정보를 가지고 스프링이 컨테이너에 넣어주고 관리할 수 있게 됨
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class); //파라미터로 AppConfig를 넣어줌. .class도 적어줘야함
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);//AppConfig의 @Bean이 붙은 "memberService"라는 이름의 메서드고, 타입이 MemberService인 걸 가져와

        Member member = new Member(1L, "memberA", Grade.VIP);//멤버 하나 생성
        memberService.join(member); //회원가입 완료

        Member findMember = memberService.findMember(1L);//방금 회원가입한 멤버를 조회하기

        //서비스 기능이 제대로 동작하는지 출력으로 확인해보기
        System.out.println("new member = " + member.getName());
        System.out.println("find Member = "+findMember.getName());

    }
}
