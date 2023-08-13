package study.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Repository
public class MemberJpaRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    //생성자 통해 엔티티 매니저 주입받음, 쿼리팩토리는 em통해 생성완료
    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);

    }


//    public MemberJpaRepository(EntityManager em, JPAQueryFactory queryFactory) { //querydslapplication에 jpaquerydsl를 빈으로 등록한 경우, 바로 해당 방식으로 주입받을 수도 있음
//        this.em = em;
//        //this.queryFactory = new JPAQueryFactory(em);
//        this.queryFactory=queryFactory;
//    }

    //회원 저장
    public void save(Member member)
    {
        em.persist(member);
    }

    //회원 단건 조회
    public Optional<Member> findById(Long id)
    {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember); //회원이 없을 경우를 대비해 optional로 감싸서 반환
    }

    //회원 전체 조회
    public List<Member> findAll(){
        return em.createQuery("select m from Member m ",Member.class)
                .getResultList();
    }

    //회원 전체 조회 - querydsl버전
    public List<Member> findAll_Querydsl(){
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    //회원 이름으로 조회
    public List<Member> findByUsername(String username){
        return em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username",username)
                .getResultList();
    }

    //회원 이름으로 조회 - querydsl버전
    public List<Member> findByUsername_Querydsl(String username){
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();

    }

    //검색조건으로 회원 조회, dto로 반환
    //booleanbuilder사용해서 동적쿼리 수행
    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition){

        //빌더 만들기
        BooleanBuilder builder = new BooleanBuilder(); //이게 필요, 초기값을 넣어줄 수 있음

        if(StringUtils.hasText(condition.getUsername())) //condition의 username필드가 널이 아니라 텍스트가 있다면
        {
            builder.and(member.username.eq(condition.getUsername()));//member.username이 condition.getUsername()랑 같은지 확인하는 조건을 builder에 추가
        }

        if(StringUtils.hasText(condition.getTeamName())) //condition의 teamName필드가 널이 아니라 텍스트가 있다면
        {
            builder.and(team.name.eq(condition.getTeamName())); //team.name이 condition.getTeamName()랑 같은지 확인하는 조건을 builder에 추가
        }

        if(condition.getAgeGoe() != null) //condition의 ageGoe필드가 널이 아니라면
        {
            builder.and(member.age.goe(condition.getAgeGoe())); //builder에 나이가 해당 값보다 크거나 같은지에 대한 조건을 추가
        }

        if(condition.getAgeLoe() != null) //condition의 ageLoe필드가 널이 아니라면
        {
            builder.and(member.age.loe(condition.getAgeLoe())); //builder에 나이가 해당 값보다 작거나 같은지에 대한 조건을 추가
        }


        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"), //@QueryProjection은 생성자 주입 방식이랑 비슷한데..? 그래도 필드이름 맞춰줘야하나보다
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder) //여기에 builder 넣어주기
                .fetch();

    }


    //검색조건으로 회원 조회, dto로 반환
    //where절 파라미터 이용해 동적쿼리 수행
    public List<MemberTeamDto> search(MemberSearchCondition condition)
    {
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team,team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                        )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ?  member.username.eq(username):null; //condition의 username필드가 널이 아니면 where문에 해당 조건 추가
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? team.name.eq(teamName) : null; //condition의 teamName필드가 널이 아니면 where문에 해당 조건 추가
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null; //condition의 ageGoe필드가 널이 아니면 where문에 해당 조건 추가
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null; //condition의 ageLoe필드가 널이 아니면 where문에 해당 조건 추가
    }
}
