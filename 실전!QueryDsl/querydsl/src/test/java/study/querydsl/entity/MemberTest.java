package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//@Transactional 어노테이션:
//@Transactional 어노테이션은 테스트 메서드 내에서 실행되는 모든 데이터베이스 조작 작업을 트랜잭션 내에서 실행하도록 지정합니다.
// 테스트가 실행되고 트랜잭션 내에서 실행되는 경우, 테스트 메서드가 완료될 때 해당 트랜잭션은 롤백됩니다.
// 이렇게 함으로써 테스트가 데이터베이스를 변경하지만 실제 데이터에는 영향을 주지 않게 됩니다.

//@Commit 어노테이션:
//@Commit 어노테이션은 테스트 메서드의 실행 이후에 트랜잭션을 커밋하도록 지정합니다.
// 기본적으로 @Transactional 어노테이션을 사용하면 테스트 메서드가 종료될 때 롤백되지만, @Commit 어노테이션을 사용하면 롤백되지 않고 커밋됩니다.

//따라서 @Transactional과 @Commit 어노테이션을 함께 사용하는 경우:
//테스트 메서드 내에서의 데이터베이스 조작은 트랜잭션 내에서 실행됩니다.
//테스트 메서드가 완료될 때 롤백되지 않고 커밋됩니다.
//이러한 조합은 일반적으로 테스트 환경에서 특정 테스트 케이스를 실행한 후 데이터베이스의 변경 내용을 확인하고자 할 때 유용할 수 있습니다.
// 하지만 주의해야 할 점은 테스트 메서드가 실행된 후 데이터베이스가 변경되므로, 다른 테스트 케이스에 영향을 줄 수 있다는 점입니다.
// 따라서 데이터베이스의 초기 상태를 고려하여 테스트를 설계하고 실행해야 합니다.
@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity(){
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

        //초기화
        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m ", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());

        }

    }

}