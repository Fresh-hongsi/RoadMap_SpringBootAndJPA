package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long>//type,pk
 {
     List<Member> findByName(String name); //이렇게 하면 jpa가 'name'이라는 걸 기반으로 찾음
     //select m from member m where m.name= ? 이란 jpql을 실행
 }
