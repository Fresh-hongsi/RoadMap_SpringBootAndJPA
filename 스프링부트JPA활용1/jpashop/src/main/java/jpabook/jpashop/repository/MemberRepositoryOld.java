package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository //컴포넌트 스캔에 의해 자동으로 스프링 빈으로 관리
@RequiredArgsConstructor
public class MemberRepositoryOld {

    //@PersistenceContext // 스프링이 jpa의 엔티티 매니저를 여기에 주입해줌
    //private EntityManager em;

    // @RequiredArgsConstructor를 사용할 것이므로 final 붙은 변수 사용
    private final EntityManager em; //@RequiredArgsConstructor로 인해 생성자가 자동으로 생김.

    //회원을 db에 저장하는 로직
    public void save(Member member) {
        em.persist(member); //jpa가 member르 저장함
    }

    //회원을 db로부터 조회하는 로직
    public Member findOne(Long id)
    {
        return em.find(Member.class,id); //두번쨰 파라미터: pk
    }

    //회원 전체를 리스트화 시켜서 뿌리는 루직
    public List<Member> findAll(){ //이떄는 jpql을 작성해야함
        //찾은 모든 멤버를 리스트화

        return em.createQuery("select m from Member m", Member.class)//1번쨰 parameter:쿼리, 2번째 parameter:반환 타입
                .getResultList();

    }

    //이름으로 회원 검색
    public List<Member> findByName(String name) //이떄는 jpql을 작성해야함
    {
        return em.createQuery("select m from Member m where m.name=:name", Member.class)
                .setParameter("name",name) //윗줄의 :name에 매개변수로 넘어온 name을 넣어줌
                .getResultList(); //결과값을 받아옴
    }
}
