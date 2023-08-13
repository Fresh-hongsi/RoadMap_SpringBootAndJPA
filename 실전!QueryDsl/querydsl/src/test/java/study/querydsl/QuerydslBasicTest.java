package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;
import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach //각 테스트 실행되기 전에 동작하는 부분
    public void before(){

        queryFactory = new JPAQueryFactory(em); //필드 레벨로 쿼리 펙토리 넣어줌

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10, teamA);
        Member member2=new Member("member2",20, teamA);
        Member member3=new Member("member3",30, teamB);
        Member member4=new Member("member4",40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL(){
        //member1을 jpql로 찾아라
        String qlString = "select m from Member m "
                            +"where m.username =: username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl(){
        //member1을 쿼리dsl로 찾아라
        //JPAQueryFactory queryFactory = new JPAQueryFactory(em);//쿼리팩토리를 가져오기(엔티티 매니저를 생성자로 넘겨줘야함)
        //위에 필드로 따로 뺴줬음
        //QMember m = new QMember("m"); //alias를 주기(jpql처럼) //별칭 직접 지정
        //QMember m = QMember.member; //기본 인스턴스 사용
        //3번째 방법: STATIC IMPORT 사용
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) //jpql은 파라미터 바인딩을 해줬는데 dsl은 이런식으로만 적어도 파라미터 바인딩이 됨
                .fetchOne();//상위 하나만 가져와라

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test //검색 조건 쿼리
    public void search(){
        Member findMember = queryFactory
//                .select(member)
//                .from(member) ->select from 합칠 수 있음
                .selectFrom(member)
                .where(member.username.eq("member1").and(member.age.eq(10))) //이름이 member1이면서 나이가 10살인 사람 조회
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);
    }

    @Test //검색 조건 쿼리
    public void searchAndParam(){
        Member findMember = queryFactory
//                .select(member)
//                .from(member) ->select from 합칠 수 있음
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)) //위의 and연산 대신 쉼표로 끊어서 가도 똑같이 등작함
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);
    }

    //결과 조회 관련
    @Test
    public void resultFetch(){

        //member의 목록 모두 조회
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        //member 목록 중 상위 하나 조회, 즉 단건 조회
        Member fetchOne = queryFactory.selectFrom(member)
                .fetchOne();

        //member 목록 중 limit 1걸고 상위 하나 조회
        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        //페이징에서 사용,: 페이징 정보 포함, total count 쿼리 추가 실행
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();//deprecated
        //results.getTotal();

        //토탈 count만 가져오기
        long total = queryFactory
                .selectFrom(member)
                .fetchCount();//deprecated

    }

    //정렬 관련
    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순 desc
     * 2. 회원 이름 올림차순 asc
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    public void sort(){
        //before each에 이어 데이터 더 추가
        em.persist(new Member(null,100));
        em.persist(new Member("member5",100));
        em.persist(new Member("member6",100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast()) //nullsFirst: 널인게 앞에 위치
                .fetch();

        //검증
        Member member5 = result.get(0);
        Member member6=result.get(1);
        Member memberNull= result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    //페이징 관련
    @Test
    public void paging1(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc()) //회원 이름 기준 내림차순
                .offset(1) //0다음인 1번쨰 인덱스부터
                .limit(2) //2개 가져오기
                .fetch();

        assertThat(result.size()).isEqualTo(2);
//        for (Member member1 : result) {
//            System.out.println("member1 = " + member1);
//        } //member4, member3 가져옴
    }

    //페이징 관련 - count쿼리도 같이하고 싶다면?
    @Test
    public void paging2(){
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc()) //회원 이름 기준 내림차순
                .offset(1) //0다음인 1번쨰 인덱스부터
                .limit(2) //2개 가져오기
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4); //offset1부터 시작했으니까 member4,3,2,1이 total임
        assertThat(queryResults.getLimit()).isEqualTo(2); //limit는 2개로 설정했으므로
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2); //member4, member3이 이번 페이징의 결과이므로 2개

    }

    //집합 관련
    @Test
    public void aggregation(){
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch(); //이런 방식으로 쿼리 날리면 tuple이 반환됨. 실무에선 tuple이 아닌, dto로 뽑는 방법 이용, 뒤에서 알려줄 것

        Tuple tuple = result.get(0);
        //이런 방식으로 tuple에서 조회하면 된다
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo((100));
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    //group by 사용하는 법

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라
     */
    @Test
    public void group() throws Exception {

        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)//member의 team과 team엔티티를 조인
                .groupBy(team.name)
                .fetch();

        //팀이 2개였으므로 튜플이 2개 나옴
        Tuple teamA = result.get(0); //teamA에 대한 튜플
        Tuple teamB = result.get(1); //teamB에 대한 튜플

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    //기본 조인 관련

    /**
     * 팀 A에 소속된 모든 회원을 조회하라
     */
    @Test
    public void join() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team) //우측의 team은 qtype의 team이다
                //.leftJoin(member.team,team) //left join도 지원
                .where(team.name.eq("teamA"))
                .fetch();
        //result의 각 Member에 대한 "username"필드가 "member1", "member2"인지 검증
        assertThat(result)
                .extracting("username")
                .containsExactly("member1","member2");

    }

    //쎼타 조인 관련(연관관계가 없어도 할 수 있는 조인)
    //회원의 이름이 팀 이름과 같은 회원 조회하기(억지성)
    //회원과 팀 테이블이 전체 조인됨
    @Test
    public void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));


        List<Member> result = queryFactory
                .select(member)
                .from(member, team) //쎼타조인
                .where(member.username.eq(team.name)) //회원 이름과 팀 이름이 같은 사람
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA","teamB");
    }

    //조인 - on절
    //ON절을 활용한 조인(JPA 2.1부터 지원)
    //1. 조인 대상 필터링하거나
    //2. 연관관계 없는 엔티티 외부 조인할 때
    //두가지에 대해 가능하게 하는 ON절

    //조인 대상 필터링
    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀과만 조인해라.
     * 그리고 회원은 모두 조회해서 출력해라 (member3, member4도)
     * jpql: select m,t from Member m left join m.team t on t.name = 'teamA'
     */
    @Test
    public void join_on_filtering(){

        List<Tuple> result = queryFactory
                .select(member,team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }


    }

    //연관관계 없는 엔티티 외부조인할 떄 쓰는 on절
    //on절이용
    // 회원의 이름이 팀 이름과 같은 회원을 외부조인해라!
    @Test
    public void join_on_no_relation() throws Exception
    {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team) //연관관계가 따로 없으므로 그냥 team만 넣어준다!. 그러면 쎼타조인처럼 막조인과 같이 동작
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }

    //페치조인을 사용하지 않는 경우의 쿼리
    @PersistenceUnit
    EntityManagerFactory emf;
    @Test
    public void fetchJoinNo(){
        em.flush();
        em.clear(); //페치 조인인 경우 영속성 컨텍스트 깔끔하게 비워둔 상태에서 테스트해보는 게 좋다

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne(); //전체 회원 중 이름이 member1인 회원 단건 조회

//        System.out.println("--------------------");
//        System.out.println("findMember.getTeam().getName: "+ findMember.getTeam().getName());

        //member.team은 lazy이기 때문에 아직까진 team과 연관없는 member의 속성만 조회됨
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());//emf의 기능 중 하나인데, 해당 어트리뷰트가 lazy로 로딩됐늕 아닌지 판단해줌

        //lazy이므로 loaded는 false가 나와야함
        assertThat(loaded).as("페치조인 미적용").isFalse();

    }

    //페치 조인 적용
    @Test
    public void fetchJoinUse(){
        em.flush();
        em.clear(); //페치 조인인 경우 영속성 컨텍스트 깔끔하게 비워둔 상태에서 테스트해보는 게 좋다

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team,team).fetchJoin() //fetchjoin임을 명시적으로 표기해야 페치조인으로 동작
                .where(member.username.eq("member1"))
                .fetchOne(); //전체 회원 중 이름이 member1인 회원 단건 조회

        //member.team은 lazy이기 때문에 아직까진 team과 연관없는 member의 속성만 조회됨
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());//emf의 기능 중 하나인데, 해당 어트리뷰트가 lazy로 로딩됐늕 아닌지 판단해줌


        //페치조인했으므로 member의 team도 세팅완료,  loaded는 true가 나와야함
        assertThat(loaded).as("페치조인 적용").isTrue();

    }

    //쿼리 안의 쿼리! 서브쿼리 적용해보기

    /**
     * 나이가 가장 많은 회원 조회하기
     */
    //서브쿼리 사용할 떄는 jpaexpression필요
    //서브쿼리 내에서는 기본 alias랑 이름이 같으면 안되므로 별도의 이름을 지정해줘야함

    @Test
    public void subQuery(){
        QMember memberSub = new QMember("memberSub"); //서브쿼리안에서 사용할 서브쿼리 alias
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    /**
     * 나이가 가장 평균 이상인 회원 조회하기
     */
    //서브쿼리 사용할 떄는 jpaexpression필요
    //서브쿼리 내에서는 기본 alias랑 이름이 같으면 안되므로 별도의 이름을 지정해줘야함
    @Test
    public void subQueryGOE(){
        QMember memberSub = new QMember("memberSub"); //서브쿼리안에서 사용할 서브쿼리 alias
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(//이상인
                        JPAExpressions
                                .select(memberSub.age.avg())//평균
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40); //평균보다 나이 많은 애들은 30,40임
    }


    /**
     * 나이가 10살 이상인 회원을 서브쿼리에서 찾아서(20,30,40) 나이가 20,30,40인 member를 바깥 쿼리에서 조회
     */
    //서브쿼리 사용할 떄는 jpaexpression필요
    //서브쿼리 내에서는 기본 alias랑 이름이 같으면 안되므로 별도의 이름을 지정해줘야함
    @Test
    public void subQueryIn(){
        QMember memberSub = new QMember("memberSub"); //서브쿼리안에서 사용할 서브쿼리 alias
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(//in절
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20,30,40); //평균보다 나이 많은 애들은 30,40임
    }

    //select절 안에서 subquery
    //모든 회원의 이름 옆에 평균 나이 출력함
    @Test
    public void selectSubQuery()
    {
        QMember memberSub = new QMember("memberSub"); //서브쿼리안에서 사용할 서브쿼리 alias
        List<Tuple> result = queryFactory
                .select(member.username,
                        JPAExpressions  //static import가능하다! alt+enter 누르면 됨
                                .select(memberSub.age.avg())
                                .from(memberSub)
                )
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    //case문
    @Test void basicCase(){
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("10살")
                        .when(20).then("20살")
                        .otherwise("기타")
                )
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    //복잡한 case문 -> caseBuilder사용
    @Test
    public void complexCase(){

        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20")
                        .when(member.age.between(21, 30)).then("21~30")
                        .otherwise("기타")
                )
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);

        }

    }

    //상수 문자 더하기
    @Test
    public void constant(){
        List<Tuple> a = queryFactory
                .select(member.username, Expressions.constant("A")) //쿼리 결과 가져올 떄 하나의 별도 필드에 A넣어줌
                .from(member)
                .fetch();

        for (Tuple tuple : a) {
            System.out.println("tuple = " + tuple);

        }
    }

    //상수 문자 더하기
    //{username}_{age} 형식으로 가져오기
    @Test
    public void concat(){
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue())) //age는 int이므로 string으로 타입 변환, emum타입일 경우도 stringvalue유용
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s); //s = member1_10
        }
    }

    //중급문법
    //프로젝션 결과 타입이 한개일떄, 타입이 list의 <>로 나옴
    @Test
    public void simpleProjection(){

        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    //프로젝션 결과 타입이 여러개일떄, 타입이 tuple(쿼리 dsl의 튜플자료)로 나옴
    //튜플은 레포지토리 계층 안에서 사용, 다른 계층으로 보낼땐, dto로 변환해서 보내는게 좋음
    @Test
    public void tupleProjection(){
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);//튜플 통해 username꺼낼때
            Integer age = tuple.get(member.age); //튜플 통해 age꺼낼 떄
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    //프로젝션 결과 반환 - dto 조회 BY 기본 JPQL
//    순수 JPA에서 DTO를 조회할 때는 new 명령어를 사용해야함
//    DTO의 package이름을 다 적어줘야해서 지저분함
//    생성자 방식만 지원함
    @Test
    public void findDtoByJPQL(){
        List<MemberDto> result = em.createQuery("select new study.querydsl.dto.MemberDto(m.username,m.age) from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //프로젝션 결과 반환 - dto 조회 BY 쿼리dsl
    //1번쨰 방식: 프로퍼티 접근 (getter,setter이 포함되어있는 @data때문에 가능한 것)
    //세터 삽입경우는 이름을 보고 들어가므로 이름을 맞춰줘야 들어간다/
    @Test
    public void findDtoBySetter(){
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age)) //이런 방식으로 하면 됨
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //프로젝션 결과 반환 - dto 조회 BY 쿼리dsl
    //2번쨰 방식: 필드 직접 접근(게터 세터 없어도 동작)
    //필드 삽입경우는 이름을 보고 들어가므로 이름을 맞춰줘야 들어간다/
    @Test
    public void findDtoByField(){
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age)) //이런 방식으로 하면 됨
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //프로젝션 결과 반환 - dto 조회 BY 쿼리dsl
    //3번쨰 방식: 생성자 사용
    //생성자의 경우는 타입을 보고 들어가므로 setter방식과 다르게 타입만 맞으면 들어간다/
    @Test
    public void findDtoByConstructor(){
        List<UserDto> result = queryFactory
                .select(Projections.constructor(UserDto.class,
                        member.username,
                        member.age)) //생성자 방식일 경우 여기 필드에 들어가는 username,age랑 dto의 필드랑 딱 타입이 맞아떨어져야함
                .from(member)
                .fetch();

        for (UserDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    //필드이름이 member와는 조금 다른 userDto를 필드직접 접근 방식으로 projection하기
    //필드 주입이므로 이름 변경 필요
    @Test
    public void findUserDto(){
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"), //이름이 안맞는다!!!->as로 변환 필요
                        member.age)) //이런 방식으로 하면 됨
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto); //userDto의 name이 null이 되어버림
        }
    }

    //필드이름이 member와는 조금 다른 userDto를 필드직접 접근 방식으로 projection하기
    //+ 서브쿼리로 user의 age필드에 age가 가장 많은 걸로 모두 세팅
    //필드 주입이므로 이름 변경 필요
    @Test
    public void findUserMaxAgeDto(){
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"), //이름이 안맞는다!!!->as로 변환 필요(간단한 경우)

                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub),"age") //as 통해 서브 쿼리 받아온 거의 field이름을 age로 맞춰줌. 서브쿼리의 경우 ExpressionUtils 이용해서 필드 이름을 맞춰줘야함
                )) //이런 방식으로 하면 됨
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto); //userDto의 name이 null이 되어버림
        }
    }

    //프로젝션과 결과 반환 - @QueryProjection (장단이 존재-> 꼭 써야할 필요는 없을듯?)
    //장점: 간편, 생성자 방식처럼 동작
    //단점: 컴파일때 오류 못잡음 , 런타임때 에러 나옴, memberDto 클래스 파일에 @QueryProjection가 들어가므로 dto가 쿼리dsl에 의존성 생김
    @Test
    public void findDtoByQueryProjection(){
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age)) //@QueryProjection의 효과로 QMemberDto의 파일이 만들어져서 이것만 넣어줘도됨
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

//    동적 쿼리를 해결하는 두가지 방식
//    1.BooleanBuilder
//    2.Where 다중 파라미터 사용

    //동적쿼리 해결방식 1 - BooleanBuilder
    @Test
    public void dynamicQuery_BooleanBuilder(){
        //동적쿼리인데, 검색 조건으로 회원 이름이 member1이고 나이가 10인 회원을 찾고 싶음
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember1(usernameParam,ageParam); //검색조건 넣은 함수를 실행해서 받아옴
        assertThat(result.size()).isEqualTo(1); //결과는 1개여야함
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        //usernameCond, ageCond가 null이어도 동작하게끔 해야함

        BooleanBuilder builder = new BooleanBuilder(); //이게 필요, 초기값을 넣어줄 수 있음
        if(usernameCond != null) //usernameCond가 어떤 값이라도 들어있으면
        {
            builder.and(member.username.eq(usernameCond)); //member.username이 usernameCond랑 같은지 확인하는 조건을 builder에 추가
            //and 대신 or조건을 넣어줄 수도 있다
        }

        if(ageCond!=null) //ageCond가 어떤 값이라도 들어있으면
        {
            builder.and(member.age.eq(ageCond)); //member.age가 ageCond랑 같은지 확인하는 조건을 builder에 추가
            //and 대신 or조건을 넣어줄 수도 있다
        }
        return queryFactory
                .selectFrom(member)
                .where(builder) //여기에 builder를 넣어주기
                .fetch();

    }

    //동적쿼리 해결방식 2 - Where 다중 파라미터 사용
    @Test
    public void dynamicQuery_WhereParam(){
        //동적쿼리인데, 검색 조건으로 회원 이름이 member1이고 나이가 10인 회원을 찾고 싶음
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember2(usernameParam,ageParam); //검색조건 넣은 함수를 실행해서 받아옴
        assertThat(result.size()).isEqualTo(1); //결과는 1개여야함
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {

        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond),ageEq(ageCond)) //usernameEq,ageEq 메서드를 직접 만들 것임. usernameEq,ageEq의 결과가 null이면 각각이 쿼리에서 무시됨
                //.where(allEq(usernameCond,ageCond)) //조합한 한방 함수로 호출도 가능!
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {

        if(usernameCond!=null) //usernameCond가 어떤 값이라도 들어있으면
        {
            return member.username.eq(usernameCond); //where문에 해당 조건 추가
        }
        else { //usernameCond가 가 널이면 where문에 널 반환
            return null;
        }
        //3항 연산자 써도 됨!

    }

    private BooleanExpression ageEq(Integer ageCond) {

        if(ageCond==null) //ageCond가 가 널이면 where문에 널 반환
        {
            return null;
        }

        else { //ageCond가 어떤 값이라도 들어있으면
            return member.age.eq(ageCond); //where문에 해당 조건 추가
        }
    }

    //만들어놓은 조건 함수들을 조합할 수 있다! -> 재사용에 용이
    private BooleanExpression allEq(String usernameCond, Integer ageCond)
    {
        return usernameEq(usernameCond).and(ageEq(ageCond)); //이름이 해당 이름과 같고, 나이가 해당 이름이 같은지에 대한 null처리 포함 동적쿼리 완성
    }

    //QueryDsl에서의 수정, 삭제 벌크 연산
    //나이가 28미만인 회원의 이름을 비회원으로 벌크연산 처리
    //결과값으로 업데이트된 row의 수가 반환됨 -> member1,member2가 영향 받았으므로 count는 2가될 것
    //bulk연산 조심해야할 점? -> db에만 반영.. 영속성컨텍스트랑 동기화가 안되어있음
    //따라서 벌크 연산 후에 em.flush, em.clear해서 동기화시켜줘야함

//    벌크연산 전:
//<영속성 컨텍스트>		<db>
//    member1: "member1"	member1: "member1"
//    member2: "member2"	member2: "member2"
//    member3: "member3"	member3: "member3"
//    member4: "member4"	member4: "member4"

//    벌크연산 후:
//<영속성 컨텍스트>		<db>
//    member1: "member1"	member1: "비회원"
//    member2: "member2"	member2: "비회원"
//    member3: "member3"	member3: "member3"
//    member4: "member4"	member4: "member4"
//
//    이후 다음을 실행: queryFactory.selectfrom(member).fetch();
//<결과>
//<영속성 컨텍스트>		<db>
//    member1: "member1"	member1: "비회원"
//    member2: "member2"	member2: "비회원"
//    member3: "member3"	member3: "member3"
//    member4: "member4"	member4: "member4"
//    why? db에서 가져올떄 영속성 컨텍스트에 올라와있는 pk값이면, db의 값으로 갱신되지 않고,
//    영속성 컨텍스트 값이 유지됨
    @Test
    //@Commit //이 테스트는 별도로 db에 반영하기 위함(롤백 안하고)
    public void bulkUpdate(){
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        em.flush(); //기존 영속성 컨텍스트를 db와 싱크 맞춤
        em.clear(); //영속성컨텍스트 비우기

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }

    }

    //벌크 연산으로 기존 숫자에 1더하기: 모든 회원의 나이를 +1시키기
    @Test
    public void bulkAdd(){
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.multiply(2)) //만약에 1 뺴고 싶으면 .add(-1)하기, 곱하기는 .multiply(1)하기
                .execute();
    }

    //벌크 연산으로  대량 데이터 삭제하기
    //18세 초과 회원의 데이터를 지워라
    @Test
    public void bulkDelete(){
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }

    //SQL function 호출하기 : SQL function은 JPA와 같이 Dialect에 등록된 내용만 호출할 수 있다
    //회원을 조회할건데, 회원의 이름 중 member가 들어간 부분을 M으로 변환할 것
    @Test
    public void sqlFunction(){
        List<String> result = queryFactory
                .select(Expressions.stringTemplate(
                        "function('replace', {0},{1},{2})", //h2방언 sql의 replace함수 호출 {0}: member.username {1}:"member" {2}:"M"으로 바인딩
                        member.username, "member", "M"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    //SQL function 호출하기 : SQL function은 JPA와 같이 Dialect에 등록된 내용만 호출할 수 있다

    @Test
    public void sqlFunction2(){
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
//                .where(member.username.eq( //회원의 이름을 소문자로 변환한게 기존의 이름과 같은 멤버를 가져오기 -> 모든 멤버 호출
//                        Expressions.stringTemplate("function('lower',{0})", member.username)))
                .where(member.username.eq(member.username.lower())) //위와똑같이 동작
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }





}
