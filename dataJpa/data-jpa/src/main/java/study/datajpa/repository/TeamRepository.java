package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;


//jpa가 이 인터페이스 보고 알아서 구현체를 꽂아버림
//@Repository 애노테이션 생략 가능
//컴포넌트 스캔을 스프링 데이터 JPA가 자동으로 처리
//JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리
public interface TeamRepository extends JpaRepository<Team,Long> {
}
