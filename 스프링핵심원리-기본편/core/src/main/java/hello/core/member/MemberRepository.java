package hello.core.member;

public interface MemberRepository {

    void save(Member member); //회원 저장

    Member findById(Long id);  //회원의 id를 가지고 회원을 찾기
}
