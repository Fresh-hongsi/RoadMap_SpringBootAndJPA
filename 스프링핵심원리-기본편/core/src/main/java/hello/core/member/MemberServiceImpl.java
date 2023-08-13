package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService{ //회원 서비스와 관련된 구현체

    //private final MemberRepository memberRepository = new MemoryMemberRepository();

    private final MemberRepository memberRepository; //위의 코드 대신 appconfig로 의존관계 설정해줄 것

    //아래의 생성자를 통해 MemberRepository의 구현체가 뭐로 들어갈지(메모리,db등등)를 결정

    @Autowired //의존관계 자동 주입을 생성자에 붙여주면 MemberRepository 타입에 맞는 애를 찾아와서 자동으로 주입해줌
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //실제 저장소 구현체를 가져옴, 사용은 인터페이스 통해서!
    //다형성을 통해 memberRepository가 save나 findById를 호출하면 실제 구현된 MemoryMemberRepository의 메서드 호출!
    //MemberServiceImpl은 MemberRepository라는 인터페이스 뿐만 아닌 MemoryMemberRepository에도 의존하는 단점이 존재!! 추상화에도, 구체화에도 의존하는 문제 발생
    //->DIP 위반
    @Override
    public void join(Member member) { //회원 가입

        memberRepository.save(member);

    }

    @Override
    public Member findMember(Long memberId) { //회원 조회
        return memberRepository.findById(memberId);
    }

    //테스트 용도로 만든 get함수
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
