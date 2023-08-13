package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;


    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("spring100");

        //when
        Long saveId = memberService.join(member); //저장됏으면 해당 멤버의 id반환


        //then
        Member findMember = memberService.findOne(saveId).get(); //해당 id에 맞는 멤버를 반환
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1= new Member();
        member1.setName("spring");

        Member member2=new Member();
        member2.setName("spring");

        //When
        memberService.join(member1);
        IllegalStateException e= assertThrows(IllegalStateException.class,()->memberService.join(member2));
        //member2를 다시 join하는 경우(우측 람다함수), IllegalStateException이라는 예외가 나오길 기대한다.

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다."); //assertThat을 통해 에외 메시지가 똑같은지 확인
        //Then
    }

}
