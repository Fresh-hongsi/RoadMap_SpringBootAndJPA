package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)//스프링이랑 아예 integration해서 하는 테스트
@SpringBootTest //스프링을 테스트를 위해 올려줌. 이게 있어야 autowired먹음
@Transactional //이게 있어여 롤백이 됨(트랜잭셔널이 @test에 있는 경우) / @test가 아닌 곳에 있으면 그냥 정상적으로 롤백없이 동작함
// @Rollback(false) 이걸 넣어주면 @test에서도 롤백 없이 동작
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(false) //이렇게 하면 transaction에서 roll back 안하고 commit까지 해서 insert 쿼리문이 나감
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member,memberRepository.findOne(saveId)); //회원 생성할 떄 사용한 member와 db에 들어간 member가 같은지 보기
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception
    {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다!! -> 여기서 예외 터져서 나간 얘가 IllegalException머시기가 위의 Expectec랑 같다면 테스트 통과


        //then
        fail("예외가 발생해야 한다"); //코드 흐름이 여기로 오면 에러 발생시킴-> 여기로 왔다는 건 예외가 발생해야 하는데 발생하지 않았다는 뜻
    }



}