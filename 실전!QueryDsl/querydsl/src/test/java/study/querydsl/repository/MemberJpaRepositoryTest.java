package study.querydsl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired EntityManager em;

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test //기본 jpa 리포지토리 동작 test
    public void basicTest(){
        Member member = new Member("member1",10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get(); //id는 generate되어있음
        assertThat(findMember).isEqualTo(member);

//        List<Member> result1 = memberJpaRepository.findAll();
        List<Member> result1 = memberJpaRepository.findAll_Querydsl(); //쿼리dsl로 변환한 함수
        assertThat(result1).containsExactly(member); //전체 조회한 result1이 member랑 똑같은 거 하나를 가지고 있는지 검증

        //List<Member> result2 = memberJpaRepository.findByUsername("member1");
        List<Member> result2 = memberJpaRepository.findByUsername_Querydsl("member1"); //쿼리dsl로 변환한 함수
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
        List<MemberTeamDto> result = memberJpaRepository.search(condition); //검색 조건에 의한 조회(다중 where)
        assertThat(result).extracting("username").containsExactly( "member4");
    }



}