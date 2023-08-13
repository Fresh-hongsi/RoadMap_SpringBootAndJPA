package hello.hellospring;

import hello.hellospring.aop.TimeTraceAop;
import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired //생략 가능
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //private DataSource dataSource;



    //@Autowired
   // public SpringConfig(DataSource dataSource) {
    //    this.dataSource = dataSource;
    //}

    //private EntityManager em;

    //@Autowired
    //public SpringConfig(EntityManager em) {
    //    this.em = em;
    //}

    @Bean //spring bean을 등록할 것임을 명시, 스프링은 config 어노테이션을 읽고
    //memberService()를 실행해 MemberService를 컨테이너에 등록한다
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

//    @Bean //MemberService생성자를 호출하기 위해서는 MemoryMemberRepositort가 필요
 //   public MemberRepository memberRepository() {
  //      //return new JdbcMemberRepository(dataSource); //그냥 jdbc
        //return new JdbcTemplateMemberRepository(dataSource); //jdbc템플릿 사용
  //      return new JpaMemberRepository(em);
   // }

    //@Bean //aop는 명사적으로 직접 빈에 등록해놓는 것이 좋다
    //public TimeTraceAop timeTraceAop() {
        //return new TimeTraceAop();
    //}
}
