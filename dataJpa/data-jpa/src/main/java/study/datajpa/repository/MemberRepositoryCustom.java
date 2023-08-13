package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;


//data jpa가 구현해주는 인터페이스 말고, mybatis, jdbcTemplate등등의 기능도 쓰고 싶다면 별도의 인터페이스 만들고, 그걸 implement하는 클래스만들어서 함수 작성
public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
