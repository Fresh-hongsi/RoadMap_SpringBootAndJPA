package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//jpa를 쓰려면 @Transactional필요
@Transactional
public class MemberService {

    //final 키워드가 붙어 있으면 값을 생성자에서 초기화 한 이후에 변경할 수 없습니다.
    private final MemberRepository memberRepository;

    //memberRepository를 memberService 내에서 만드는게 아니라,
    //외부에서 memberservice를 생성할 때 같이 넣어주도록 만든다


    public MemberService(MemberRepository memberRepository)
    {
        this.memberRepository=memberRepository;
    }

    /*
    회원가입
     */
    public Long join(Member member)
    {




            //같은 이름이 있는 중복 회원은 비즈니스 로직에서 허용 x
            validateDuplicateMember(member); //중복회원 검증
            memberRepository.save(member);
            return member.getId();


    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> { //만약 이름이 같은 회원이 있다면, 그 m에 대해 예외처리
                     throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers()
    {



            return memberRepository.findAll();


    }



    //한 회원의 id를 참조해 멤버를 찾아냄
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
