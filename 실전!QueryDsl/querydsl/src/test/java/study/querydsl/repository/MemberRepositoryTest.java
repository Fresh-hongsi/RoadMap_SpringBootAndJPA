package study.querydsl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired MemberRepository memberRepository;

    @Test //data jpa 리포지토리 동작 test
    public void basicTest(){
        Member member = new Member("member1",10);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get(); //id는 generate되어있음
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberRepository.findAll();
//        List<Member> result1 = memberRepository.findAll_Querydsl(); //쿼리dsl로 변환한 함수
        assertThat(result1).containsExactly(member); //전체 조회한 result1이 member랑 똑같은 거 하나를 가지고 있는지 검증

        List<Member> result2 = memberRepository.findByUsername("member1");
        //List<Member> result2 = memberRepository.findByUsername_Querydsl("member1"); //쿼리dsl로 변환한 함수
        assertThat(result2).containsExactly(member);

    }

    @Test //검색 조건에 의한 쿼리가 동작하는지에 대한 테스트
    public void searchTest(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10, teamA);
        Member member2=new Member("member2",20, teamA);
        Member member3=new Member("member3",30, teamB);
        Member member4=new Member("member4",40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition condition = new MemberSearchCondition();//검색 조건 생성
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");
        //회원의 이름에 대한 검색조건은 일부러 누락
        //List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition); //검색 조건에 의한 조회(빌더)
        List<MemberTeamDto> result = memberRepository.search(condition); //검색 조건에 의한 조회(다중 where) -> data jpa인터페이스에 있는 search호출(엄밀히 말하면 사용자정의 레포에 있는 함수임)
        assertThat(result).extracting("username").containsExactly( "member4");
    }

    @Test //검색 조건에 의한 쿼리가 동작하는지에 대한 테스트 + page자료형으로 반환받는 거 확인
    public void searchPageSimple(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10, teamA);
        Member member2=new Member("member2",20, teamA);
        Member member3=new Member("member3",30, teamB);
        Member member4=new Member("member4",40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition condition = new MemberSearchCondition();//검색 조건 생성(비어있는 검색 조건)->페이징이 제대로 동작하는 지 확인하려고
        PageRequest pageRequest = PageRequest.of(0, 3); //offset:0 ,limit:3 설정
//        condition.setAgeGoe(35);
//        condition.setAgeLoe(40);
//        condition.setTeamName("teamB");
        //회원의 이름에 대한 검색조건은 일부러 누락

        Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition,pageRequest); //pageRequest를 넘기면 pageable이 받음

        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getContent()).extracting("username").containsExactly("member1","member2","member3");

    }

    //SPRING DATA JPA가 제공하는 쿼리DSL관련 인터페이스 활용하는 부분 - > 실무 사용x (조인 불가능)
    @Test
    public void querydslPredicateExecutorTest(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10, teamA);
        Member member2=new Member("member2",20, teamA);
        Member member3=new Member("member3",30, teamB);
        Member member4=new Member("member4",40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);


        QMember member = QMember.member;
        Iterable<Member> result = memberRepository.findAll(member.age.between(10, 40).and(member.username.eq("member1")));//findAll내부에 바로 쿼리dsl문법 추가 가능
        for (Member findMember : result) {
            System.out.println("findMember = " + findMember);
        }
    }

}