package study.querydsl.repository;

//기존의 순수 jpa 리포지토리에서의 전환 - 스프링 데이터 jpa로 옮기기

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import study.querydsl.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom, QuerydslPredicateExecutor<Member> { //사용자 정의 레포지토리(인터페이스)를 상속함, SPRING DATA JPA가 제공하는 쿼리DSL관련 인터페이스도 상속하기

    //save, findbyid, findall은 data jpa가 기본으로 제공함

    //findByUsername은 직접 작성 - 메서드 이름으로 찾아오는 전략 사용
    //select m from Member m where m.username=? 으로 동작
    List<Member> findByUsername(String username);


}
