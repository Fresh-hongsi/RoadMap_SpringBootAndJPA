package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);  //회원을 저장소에 저장시키기

    //Optional이란? findbyid,findbyname으로 가져오는데, 널값일 수 있으므로,
    //널 값이면 optional이란 걸로 널을 감싸서 반환
    Optional<Member> findById(Long id); //id기반으로 회원 찾기

    Optional<Member> findByName(String name); //이름 기반으로 회원 찾기

    List<Member> findAll(); //저장된 모든 멤버를 리스트 형태로 반환
}
