package study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

public class MemberRepositoryImpl implements MemberRepositoryCustom{ //이름들은 관례를 따르자! ---Impl, ---Custom

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) { //빈 등록이 아닌 방식 사용했음 여기서
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
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

    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset()) //pageable이 제공하는 자동 offset사용. 값은 pageable에 들어있는 필드에서 가져옴
                .limit(pageable.getPageSize()) //한번 조회할떄 몇개까지 조회할거야! - pageable이 제공하는 자동 limit사용. 값은 pageable에 들어있는 필드에서 가져옴
                .fetchResults();//쿼리도 나가고, 카운트 쿼리도 나감

        List<MemberTeamDto> content = results.getResults(); //페이징된 MemberDto의 리스트 반환
        long total = results.getTotal(); //fetchResults()에 의해 나간 count쿼리에 영향 받은 행 수(전체 member수가 4명이므로 total= 4)
        System.out.println("-------------------"+total); //4
        return new PageImpl<>(content,pageable,total); //페이지 자료형으로 반환


    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset()) //pageable이 제공하는 자동 offset사용. 값은 pageable에 들어있는 필드에서 가져옴
                .limit(pageable.getPageSize()) //한번 조회할떄 몇개까지 조회할거야! - pageable이 제공하는 자동 limit사용. 값은 pageable에 들어있는 필드에서 가져옴
                .fetch();//카운트 쿼리는 따로 나가지 않음. 페이징된 컨텐츠만 가져옴

//        //카운트 쿼리를 별도 작성(join필요로 하지 않을 경우 성능 향상 기대)
//        long total = queryFactory
//                .select(member)
//                .from(member)
//                .leftJoin(member.team, team)
//                .where(
//                        usernameEq(condition.getUsername()),
//                        teamNameEq(condition.getTeamName()),
//                        ageGoe(condition.getAgeGoe()),
//                        ageLoe(condition.getAgeLoe())
//                )
//                .fetchCount();//count쿼리로 나감

        //카운트 쿼리를 별도 작성(join필요로 하지 않을 경우 성능 향상 기대) -> 최적화 시 다음과 같이 변경해서 사용
        JPAQuery<Member> countQuery = queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );//카운트 쿼리를 호출은 안하고, 정의만 함


        //return new PageImpl<>(content,pageable,total); //카운트쿼리 날린 결과, 페이지 조회 결과를 넣어서 페이지 자료형으로 반환

        //카운트 쿼리 최적화하는 방법임
//        스프링 데이터 라이브러리가 제공,
//        count 쿼리가 생략 가능한 경우 생략해서 처리함
//        1.페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때
//        2. 마지막 페이지 일 때 (offset + 컨텐츠 사이즈를 더해서 전체 사이즈 구함, 더 정확히는 마지막 페이지이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때)
        return PageableExecutionUtils.getPage(content,pageable,()->countQuery.fetchCount()); //여기서 springdata가 보고 카운트 쿼리 날릴지 결정
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
