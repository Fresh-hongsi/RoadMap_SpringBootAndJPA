package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

//data jpa기본 인터페이스랑, 커스텀 인터페이스를 둘다 상속받기
public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    //List<Member> findByUsername(String username);

    //이름 & 나이가 x이상인 사람들 조회하는 data jpa쿼리
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    //spring data jpa에서 namedquery를 편하게 호출하는 방법
    //@Query(name="Member.findByUsername") //신기한건 data jpa는 이게 없어도 동작함 근데 우리는 명시적으로 적어주는 습관을 가지자
    //namedquery의 장점은? 컴파일 시점에 문법 오류를 잡아준다는 것!!!
    List<Member> findByUsername(@Param("username") String username); //함수이름은아무렇게나 적어도됨. 어차피 member entity에 등록한 namedquery를 실행할 것이므로

    //data jpa에서 바로 쿼리 만들어서 실행하는 기능(리포지토리에 쿼리를 바로 지정할 수 있다)
    //컴파일 시점에 문법오류 체크 가능
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //datajpa 레포지토리에서 값, dto조회하기
    //모든 회원의 이름 리스트 가져오기
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //Merber을 dto로 조회
    //jpql쿼리 날라가서 얻은 entity의 필드들을 dto의 생성자에 주입
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //컬렉션을 파라미터 바인딩으로 엮기
    //names에 들어있는 이름들과 같은 member들을 조회
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    //모든 회원 - 컬렉션으로 조회
    List<Member> findListByUsername(String username);

    //단건
    Member findMemberByUsername(String username);

    //단건 Optional
    Optional<Member> findOptionalMemberByUsername(String username);

    //page쓸때 count쿼리도 같이 나가는 건 불필요할 떄도 있음(left outer join하면 어차피 count값은 안바뀌기 때문에)
    //따라서 page에서 count쿼리만 제거하는 방법이 있음
    //여기서 @Query는 이런식으로 카운트쿼리랑 일반 쿼리랑 분리할 수 있다는 걸 설명하고 있음
    //따라서 findByAge쿼리와 무관하다!!
//    @Query(value = "select m from Member m left join m.team t", //일반쿼리
//                countQuery = "select count(m) from Member m") //카운트쿼리
    //@Query(value="select m from Member m")
    Page<Member> findByAge(int age, Pageable pageable); //datajpa의 인터페이스인 pageable을 꼭 import할것!!!!!!
    //Slice<Member> findByAge(int age, Pageable pageable);
    //List<Member> findByAge(int age, Pageable pageable);


    //벌크성 쿼리를 data jpa에서 날리기
    @Modifying(clearAutomatically = true) //이게 있어야 순수 jpa의 executeUpdate같은게 호출돼서 업데이트 된 행 수 가져옴, clear~~true: 벌크 쿼리 끝나면 em.clear대신 호출
    @Query("update Member m set m.age=m.age+1 where m.age>=:age")
    int bulkAgePlus(@Param("age") int age);


    //페치조인 활용한 data jpa
    //member 조회할 떄 team도 같이 한방 조회해버림
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //findAll함수 override해서 fetch join구현하기
    @Override
    @EntityGraph(attributePaths = {"team"}) //이렇게만 적어주면 회원 조회할때 team도 같이 깔끔하게 조회해온다!!!
    List<Member> findAll();

    //신기한 기능 jpql이랑 fetch join둘다 사용 가능 -> 바로 위 함수랑 똑같은 기능 수행
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();


    //@EntityGraph("Member.all") //namedEntityGraph로도 할 수 있다고만 알아두자! 잘 사용x
    //이렇게 하면 username와 일치하는 member를 db에서 찾고, 그 member의 팀까지 함께 조회하는 기능
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    //hibernate에게 힌트를 제공해서 readonly로 수행하도록 함
    @QueryHints(value = @QueryHint(name="org.hibernate.readOnly", value="true"))
    Member findReadOnlyByUsername(String username);

    //jpa에서 lock기능 사용 -> 나중에 필요시 참고
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    //특정 필드만 추출해서 가져오는 기능 -> usernameonly란 인터페이스에서 명시해둔 메서드를 보고 딱 username만 가져옴
    //List<UsernameOnly> findProjectionsByUsername(@Param("username") String username); //인터페이스 방식
    <T>List<T> findProjectionsByUsername(@Param("username") String username,Class<T> type); //클래스 방식->인터페이스 방식으로 변경함


    //data jpa에서 native query 직접 쓰는 법
    //네이티브 SQL을 DTO로 조회할 때는 JdbcTemplate or myBatis 권장
    @Query(value ="select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);


    //그러나 Projections기능 도입되면서 native query를 써도 괜찮아졌음, paging도 됨. paging은 count쿼리도 날려줘야함.
    //dto로 반환됨
    @Query(value = "select m.member_id as id, m.username, t.name as teamName from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);

}
