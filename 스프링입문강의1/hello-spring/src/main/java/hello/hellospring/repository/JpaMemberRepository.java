package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

//jpa를 쓰려면 @Transactional필요 -> jpa는 join들어올 때 모든 데이터 변경이 트랜잭션 안에서 실행되어야함
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em; //jpa는 엔티티매니저라는 걸로 모든게 동작하는 구조임

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member); // jpa가 인서트 쿼리를 알아서 만들어서 db에 member를 영구히 저장
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member= em.find(Member.class,id); //매개변수: 식별자 타입, pk
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name=:name",Member.class)
                .setParameter("name",name) //:name을 실제 들어온 name값으로 치환해 쿼리 생성
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m",Member.class) //m과 같은 객체(Member)를 찾아서 반환해라
                .getResultList();
    }
}
