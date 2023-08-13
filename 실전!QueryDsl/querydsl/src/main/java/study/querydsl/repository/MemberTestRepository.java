package study.querydsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.repository.support.Querydsl4RepositorySupport;

import java.util.List;

import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

@Repository
public class MemberTestRepository extends Querydsl4RepositorySupport {

    public MemberTestRepository() {
        super(Member.class);
    }

    public List<Member> basicSelect(){
        return select(member)
                .from(member)
                .fetch();
    }

    public List<Member> basicSelectFrom(){
        return selectFrom(member)
                .fetch();
    }

    public Page<Member> searchPageByApplyPage(MemberSearchCondition condition, Pageable pageable) ///1
    {
        JPAQuery<Member> query = selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe((condition.getAgeGoe())),
                        ageLoe(condition.getAgeLoe())
                );

        List<Member> content = getQuerydsl().applyPagination(pageable,query)
                .fetch();

        return PageableExecutionUtils.getPage(content,pageable,query::fetchCount);


    }

    public Page<Member> applyPagination(MemberSearchCondition condition, Pageable pageable) ///2 : 1,2는 같은 동작하는 코드다
    {

        return applyPagination(pageable, query
                -> query.selectFrom(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe((condition.getAgeGoe())),
                        ageLoe(condition.getAgeLoe())
                ));

    }

    public Page<Member> applyPagination2(MemberSearchCondition condition, Pageable pageable) ///3 :
    {

        return applyPagination(pageable, contentQuery
                -> contentQuery.selectFrom(member) //컨텐츠용 쿼리
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe((condition.getAgeGoe())),
                        ageLoe(condition.getAgeLoe())
                ),
                countQuery->countQuery //카운트 쿼리
                        .select(member.id)
                        .from(member)
                        .leftJoin(member.team, team)
                        .where(
                                usernameEq(condition.getUsername()),
                                teamNameEq(condition.getTeamName()),
                                ageGoe((condition.getAgeGoe())),
                                ageLoe(condition.getAgeLoe())
                        )

        );

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

