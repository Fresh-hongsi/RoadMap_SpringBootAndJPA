package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired MemberRepository memberRepository;

    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB= new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamA);
        Member member3 = new Member("member3",30,teamB);
        Member member4 = new Member("member4",40,teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush(); //db에 강제로 쌓여잇던 쿼리 날려버리기
        em.clear(); //영속성 컨텍스트 깔끔히 지우기

        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam());
        }

//        List<Team> teams = em.createQuery("select t from Team t", Team.class)
//                .getResultList();
//
//        for (Team team : teams) {
//            System.out.println("Team: " + team.getName());
//            for (Member member : team.getMembers()) {
//                System.out.println(" - Member: " + member.getUsername());
//            }
//
//        }
    }

    //생성일자, 수정일자를 값타입으로 멤버에 넣어놓고 확인하는 테스트
    @Test
    public void JpaEventBaseEntity() throws Exception  {

        //given
        Member member = new Member("member1");
        memberRepository.save(member); //이때 @Prepersist발생 (jpabaseENTITY 기준)

        Thread.sleep(100);
        member.setUsername("member2"); //이름 수정

        em.flush(); //@PreUpdate발생 (jpabaseENTITY 기준)
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
//        System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
//        System.out.println("findMember.getUpdatedDate() = " + findMember.getLastModifiedDate());
//        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
//        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
    }
}