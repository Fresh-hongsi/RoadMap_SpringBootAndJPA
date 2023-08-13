package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

//순수 jpa기반 회원 엔티티 crud -> update는 변경감지 기능 사용하므로 여기에 없음!
@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    //회원 가입
    public Member save(Member member)
    {
        em.persist(member);
        return member;
    }

    //회원 삭제
    public void delete(Member member)
    {
        em.remove(member);
    }

    //회원 전체 조회
    public List<Member> findAll()
    {
        List<Member> result = em.createQuery("select m from Member m ", Member.class)
                .getResultList();
        return result;
    }

    //optional을 활용한 회원 단건 조회
    public Optional<Member> findById(Long id)
    {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); //회원이 없으면 null값을 옵셔널로 감싸서 나감
    }

    //전체 회원의 수 조회
    public long count(){
        return em.createQuery("select count(m) from Member m",Long.class).getSingleResult();
    }

    //회원 조회
    public Member find(Long id)
    {
        return em.find(Member.class,id);
    }

    //특정 이름과 나이가 그 이상인 사람들 조회
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age)
    {
        return em.createQuery("select m from Member m where m.username=:username and  m.age> :age" )
                .setParameter("username",username)
                .setParameter("age",age)
                .getResultList();
    }

    public List<Member> findByUsername(String username)
    {
        return em.createNamedQuery("Member.findByUsername",Member.class) //Member 엔티티에 정의해놨던 namedquery를 불러와서 파라미터로 넣어줌
                .setParameter("username",username)
                .getResultList();
    }

    //순수 jpa로 페이징 쿼리 만들기
//    검색 조건: 나이가 10살
//    정렬 조건: 이름으로 내림차순
//    페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 3건
    public List<Member> findByPage(int age, int offset, int limit)
    {
        return em.createQuery("select m from Member m where m.age =: age order by m.username desc")
                .setParameter("age",age)
                .setFirstResult(offset) //어디서부터
                .setMaxResults(limit) //몇개 가져올꺼야
                .getResultList();
    }

    //해당 age인 사람이 몇명이야?
    public long totalCount(int age){
        return em.createQuery("select count(m) from Member m where m.age=:age",Long.class)
                .setParameter("age",age)
                .getSingleResult();
    }

    //벌크성 수정 쿼리(순수 jpa에서)
    //파라미터로 넘어온 아니보다 같거나 많은 회원의 나이를 +1시켜라를 한방에 날리기
    public int bulkAgePlus(int age)
    {
        int resultCount = em.createQuery("update Member m set m.age=m.age+1 where m.age>=:age")
                .setParameter("age",age)
                .executeUpdate(); //이걸 하면 응답값의 개수가 나옴

        return resultCount;
    }
}
