package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//인터페이스를 인터페이스로 구현할 떄는 extends
//아래에서 Long은 멤버 클래스의 primary key인 id의 타입
public interface SpringDataJpaMemberRepository extends JpaRepository<Member,Long>,MemberRepository  {

    @Override
    Optional<Member> findByName(String name);
}
