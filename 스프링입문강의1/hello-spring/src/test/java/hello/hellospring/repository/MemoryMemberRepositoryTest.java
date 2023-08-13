package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!

class MemoryMemberRepositoryTest {

    @AfterEach //각 테스트가 끝날때마다 동작
    public void afterEach() {
        repository.clearStore(); //한 테스트가 끝날때마다 리포지토리 비워주는 함수 호출
    }
    MemoryMemberRepository repository= new MemoryMemberRepository();

    @Test //save에 대한 test코드
    public void save()
    {
        Member member= new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get(); //optional로 감싸진 객체를 하나 까서 가져옴
        //System.out.println("result = "+(result==member)); //넣은 member와 find한 멤버가 같다면 true반환
        assertThat(result).isEqualTo(member); //assertj기반 assertion -> result가 member와 같은지 확인
        //assertThat(result).isEqualTo(null); //값이 다르면 테스트 실패
    }


    @Test //findByName에 대한 test코드
    public void findByName()
    {
        Member member1=new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        assertThat(result).isEqualTo(member1); //member1과 findByName을 한게 똑같은지 확인->참


    }

    @Test //findAll에 대한 test코드
    public void findAll()
    {
        Member member1=new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2=new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result= repository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }
}
