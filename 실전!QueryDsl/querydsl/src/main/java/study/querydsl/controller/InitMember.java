package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local") //active: local인 yml파일 기반으로 동작
@Component
@RequiredArgsConstructor
public class InitMember { //멤버나 팀에 관한 세팅사항을 미리 반영하기 위함(APPLICATION에서)

    private final InitMemberService initMemberService;

    @PostConstruct //static클래스인 initMemverService를 InitMember에 주입받고, post생성자로 initMemberservice의 init메서드 실행
    //postconstruct와 transactional 어노테이션을 같이 쓸 수없어서 별도로 분리했음
    public void init(){
        initMemberService.init();
    }

    @Component
    static class InitMemberService{
        @PersistenceContext private EntityManager em;

        @Transactional //데이터 조기화
        public void init(){
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");

            em.persist(teamA);
            em.persist(teamB);

            for(int i=0;i<100;i++)
            {
                Team selectedTeam = i%2 ==0 ? teamA:teamB; //홀수번째: teamB소속, 짝수번쨰: teamA소속
                em.persist(new Member("member"+i, i , selectedTeam)); //회원 세팅

            }
        }
    }
}
