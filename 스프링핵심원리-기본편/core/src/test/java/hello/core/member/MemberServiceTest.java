package hello.core.member;

import hello.core.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat; //이 방식으로써야함!!!!
public class MemberServiceTest {

    //MemberService memberService = new MemberServiceImpl(); // 테스트에 사용할 멤버서비스 가져오기
    MemberService memberService;

    @BeforeEach //각 테스트 전에 무조건 실행
    public void beforeEach()
    {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @Test //JUNIT으로 테스트하기 위해 필요한 어노테이션
    void join()
    {
        //given
        Member member = new Member(1L,"memberA",Grade.VIP);

        //when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then
       assertThat(member).isEqualTo(findMember);
    }
}
