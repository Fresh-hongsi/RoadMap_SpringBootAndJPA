package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository; //data jpa인터페이스를 확장시킨 MemberRepository가져옴
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);//data jpa가 제공하는 함수

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 test
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //count 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);


    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);


        //이런식으로 result의 0번째 인덱스를 조회할 수 있음
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);


    }

    @Test
    public void findHelloBy() { //find와 by사이에 hello는 아무 의미 없음
        List<Member> helloBy = memberRepository.findTop3HelloBy(); //전체 member 데이터에서 상위인덱스 3개 가져오기
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //System.out.println(" ==================="); //여기서 m1,m2가 db에 실제로 저장
        List<Member> result = memberRepository.findByUsername("AAA");
        // System.out.println(" ===================");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);


    }


    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);


    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }


    }


    @Test
    public void findMemberDto() {

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }


    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }


    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        Member findMember = memberRepository.findMemberByUsername("sdasdas");
        System.out.println("findMember = " + findMember); //단건 조회이므로 null반환

        Optional<Member> findMember2 = memberRepository.findOptionalMemberByUsername("sdasdas");
        System.out.println("findMember = " + findMember2); //단건 optional 조회이므로 empty반환

        List<Member> findMember3 = memberRepository.findListByUsername("sdasdas");
        System.out.println("findMember = " + findMember3); //컬렉션 조회이므로 '빈 컬렉션'을 반환

//        조회 결과가 많거나 없으면?
//        <컬렉션>
//          결과 없음: 빈 컬렉션 반환
//          있으면: 들어있는 컬렉션 반환
//        <단건 조회>
//          결과 없음: 그냥 단건: null 반환 / optional일 경우 empty반환
//          결과가 2건 이상: joptional로 하든, optional없는 단건으로 하든avax.persistence.NonUniqueResultException 예외 발생


    }

    @Test
    public void paging() {

        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));//현재 페이지(0페이지)에서 3개 갖고와!, 그리고 정렬기준은 회원이름으로 내림차순

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);//반환타입을 page로 받으면 totalcount 관련 쿼리도 같이 날려줌
        //Slice<Member> page = memberRepository.findByAge(age,pageRequest); //모바일 디바이스에서 사용, 만약 3개 가져오라고 했으면 linit를 4로 설정
        //List<Member> page = memberRepository.findByAge(age,pageRequest); //리스트로 받으면 페이징이 아닌 단순 limit지점까지 쿼리해서 결과리스트반 반환
        // long totalCount = memberRepository.totalCount(age);


        //중요!!!!! 여기서 Page<Member> page를 그냥 api로 반환하면 엔티티를 노출시키는 것이므로 dto로 변환해야함
        //컬렉션을 dto로 변환하는 간단한 방법
        //얘를 외부에 반환하기
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        //then
        List<Member> content = page.getContent(); //페이징된 데이터를 꺼내기(TOTAL COUNT 쿼리도 날라감)
        //long totalElements = page.getTotalElements();//total data 카운트 값 가져오기

//        for (Member member : content) {
//            System.out.println("member = " + member);
//        }
//
//        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        //assertThat(page.getTotalElements()).isEqualTo(5); //slice엔 없는 기능
        assertThat(page.getNumber()).isEqualTo(0); //심지어 page기능에 page번호도 가져올 수 있는 기능이 있다!
        //assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지는 몇개인지도 구별 가능(5개 데이터, 3개씩 잘라오라고 했으므로 2) //slice엔 없는 기능
        assertThat(page.isFirst()).isTrue(); //당연히 변수 page는 첫번째 page니까 true
        assertThat(page.hasNext()).isTrue(); //0번째 page인 변수 page 바로 다음 page가 존재하냐? 전체 page는 0,1 두개이므로 true


    }

    @Test
    public void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        //20살 이상인 사람의 나이를 다 +1시키기 -> 업데이트된 행 수는 3개임
        int resultCount = memberRepository.bulkAgePlus(20);
        //벌크 연산 후에 flush, clear연산 꼭 해주기! 그래야 아래에서 findByUsername호출했을떄 db에서 새로 가져와서 제대로 된 값 가져온다
        //주의! memberRepository내에서 em이 존재하고 , teamRepository, test클래스에도 entityManager를 맨 윗줄보면 따로 가져왔는데,
        //같은 트랜잭션 내에서 em을 쓰면 memberRepository나, teamRepository나, 테스트 클래스나 같은 em을 쓴다!!
        em.flush();
        //em.clear(); //이거 대신에 @Modifying에서 clearAutomatically=true적어주면 clear자동으로 해줌


        //벌크성 쿼리를 조심해야할 것. 쿼리를 실행하면 db에 직격타로 update되어버리기 떄문에 영송성컨텍스트가 관리하고 있던 위의 member 5개와 db의 data가 일관성이 깨짐
        //위의 member들은 update안되어있음
        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0); //얘의 나이는 40살임!!! 41살이어야되는데!!! 따라서 벌크 연산하면 영속성 컨텍스트를 날려줘야함
        System.out.println("member5 = " + member5);

        //then
        assertThat(resultCount).isEqualTo(3);

    }


    //엔티티 그래프 관련
    @Test
    public void findMemberLazy() {
        //given
        //member1은 teamA참조
        //member2은 teamB참조

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        //List<Member> members = memberRepository.findAll(); //영속성 컨텍스트 깔끔이 비워진 상태에서 전체 회원 가져옴
        //List<Member> members = memberRepository.findMemberFetchJoin(); //패치 조인할 경우 아래의 getCLass도 프록시가 아닌 진짜 엔티티 가져옴
        //근데 항상 @query위에 jpql을 써줄 순 없는 노릇! 따라서 @EntityGraph이용하는 방법 이제 사용할 것 -> data jpa의 기본제공 인터페이스 findAll함수 override하기
        //List<Member> members = memberRepository.findAll(); //엔티티 그래프 활용한 페치조인 다시 사용
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());//얘는 바로 가져옴
            System.out.println("member.getTeam.getClass = " + member.getTeam().getClass()); //바로가져옴, member의 team에 대한 프록시만 가져옴
            System.out.println("member.getTeam.getName = " + member.getTeam().getName()); //얘는 지연로딩이므로 이걸 호출하는 시점에 쿼리 날라감
        }
        //then
    }


    //jpa hint->JPA 쿼리 힌트(SQL 힌트가 아니라 JPA 구현체에게 제공하는 힌트) ex)readOnly hint
    @Test
    public void queryHint() {

        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); //member1을 db에 저장
        em.clear(); //영속성 컨텍스트 비우기

        //when
        //Member findMember = memberRepository.findById(member1.getId()).get(); //member1을 db에서 가져오기
        Member findMember = memberRepository.findReadOnlyByUsername("member1"); //readonly 힌트 통해 조회목적으로 동작
        findMember.setUsername("member2"); //readonly로 되어있으므로 얘를 적어도 이 명령을 무시함

        em.flush(); //변경감지 동작 실행->db에 업데이트 쿼리 날라감

        //db에 변경을 명령하는 것이 아니라 단순 조회 목적이라면? 더 성능 최적화된 readOnly기능 사용하면 됨
    }

    @Test //jpa lock기능 test
    public void lock() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); //member1을 db에 저장
        em.clear(); //영속성 컨텍스트 비우기

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");

    }

    //data jpa의 인터페이스가 아닌, 별도의 인터페이스에 override한 클래스를 불러와서 함수 실행
    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom(); //실제 기능을 구현한 MemberRepositoryImpl의 함수가 호출됨
        //다만 주의점은, 별도의 인터페이스 이름을 MemberRepository 인터페이스가 기본 이름이라면, +구현 클래스 이름은 MemberRepositoryImpl이란 이름으로 지어줘야 동작한다!!!!
        //최근엔 MemberRepositoryCustomImpl이런식으로 해도 동작한다! 강의록참고
    }

    //jpa criteria사용 예시(거르자)
    @Test
    public void specBasic() {

        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        assertThat(result.size()).isEqualTo(1);
    }

    //Query By Example사용(inner join만 가능) 예시(거르자) 실무에선 쿼리dsl사용한다!
    @Test
    public void queryByExample() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        //Probe
        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");//age가 자동으로검색조건에 들어가는 걸 없애기

        Example<Member> example = Example.of(member, matcher);//특정 member자체를 검색 조건에 넣어버리기

        List<Member> result = memberRepository.findAll(example);//나옴

        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    //    엔티티 대신에 DTO를 편리하게 조회할 때 사용
//    전체 엔티티가 아니라 만약 회원 이름만 딱 조회하고 싶으면? -> projection사용
    @Test
    public void projections() {

        //given
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1", NestedClosedProjections.class);

        for (NestedClosedProjections nestedClosedProjections : result) {
            String username = nestedClosedProjections.getUsername();
            System.out.println("username = " + username);
            String teamName = nestedClosedProjections.getTeam().getName();
            System.out.println("teamName = " + teamName);
        }
        //then
    }

    //스프링 데이터 JPA 기반 네이티브 쿼리
    @Test
    public void nativeQuery() {

        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));//0페이지에서 10개 가져와
        List<MemberProjection> content = result.getContent();//페이징까지 된 결과물 가져오기
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());

        }

        //then
    }

}





