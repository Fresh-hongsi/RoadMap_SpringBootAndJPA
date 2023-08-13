package hello.core.member;

public interface MemberService { //회원 서비스에 대한 인터페이스

    void  join(Member member); //회원 가입과 관련된 서비스

    Member findMember(Long memberId); //회원 조회와 관련된 서비스
}
