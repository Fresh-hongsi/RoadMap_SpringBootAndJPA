package hellojpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity //이걸 넣어야 JPA가 처음 로딩될때 JPA를 사용하는구나라고 인식
//@Table(name="MBR") //만약 이게 적혀있으면 USER라는 DB테이블에 INSERT쿼리 나감
//@SequenceGenerator(name="MEMBER_SEQ_GENERATOR" , sequenceName = "MEMBER_SEQ",initialValue = 1, allocationSize = 50)
//@TableGenerator(name="MEMBER_SEQ_GENERATOR", table="MY_SEQUENCES", pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member {

    //member table이랑 attribute는 똑같이 세팅, 최소한 jpa에게 pk가 뭔지는 알려줘야함
    //@Id //Long id가 pk임을 jpa에게 공지
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //db에서 pk값을 자동생성하도록 위임
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Id @GeneratedValue
    @Column(name="MEMBER_ID")
    private Long id;

    //@Column(name="userName") //만약 이게 적혀있으면 USER TABLE의 USERNAME이랑 이 코드의 name이랑 매핑됨
    //@Column(name="name", nullable = false) //만약 이게 적혀있으면 db의 Member TABLE의 name이랑 이 코드의 username이랑 매핑됨, nullable:false->not null효과
    @Column(name="USERNAME")
    private String username;

//    @Embedded //Period class는 임베더블, Member class는 임베디드!
//    private Period workPeriod;
//
//    @Embedded //address class는 임베더블, Member class는 임베디드!
//    private Address homeAddress;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Embedded
    private Address homeAddress;

    @ElementCollection //매핑을 위해
    @CollectionTable(name="FAVORITE_FOOD",joinColumns = @JoinColumn(name="MEMBER_ID")) //테이블 명 지정-> MEMBER_ID를 외래키로 조인할 것임
    @Column(name="FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();


//    @ElementCollection //매핑을 위해
//    @CollectionTable(name="ADDRESS",joinColumns = @JoinColumn(name="MEMBER_ID")) //테이블 명 지정-> MEMBER_ID를 외래키로 조인할 것임
//    private List<Address> addressHistory = new ArrayList<>();//값 타입의 컬렉션을 멤버의 어트리뷰트로 저장

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //값타입컬렉션은 좋은 방법이 아님 -> 값타입을 엔티티로 감싸고, 일대다로 운영. 다른 방법으로는 다대일 양방향 관게도 고려 가능하나, 여기선 일대다 단방향으로 풀겠음
    @JoinColumn(name="MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }

    //
//    public List<Address> getAddressHistory() {
//        return addressHistory;
//    }
//
//    public void setAddressHistory(List<Address> addressHistory) {
//        this.addressHistory = addressHistory;
//    }

    //    public Period getWorkPeriod() {
//        return workPeriod;
//    }
//
//    public void setWorkPeriod(Period workPeriod) {
//        this.workPeriod = workPeriod;
//    }
//
//    public Address getHomeAddress() {
//        return homeAddress;
//    }
//
//    public void setHomeAddress(Address homeAddress) {
//        this.homeAddress = homeAddress;
//    }

    ////    @Column(name="TEAM_ID")
////    private Long teamId;
//
//    @ManyToOne(fetch = FetchType.LAZY) //LAZY: 프록시 통해 member class만 db에서 조회한다는 뜻/ EAGER: 즉시 MEMBER TEAM둘다 조회하겠다는 뜻
//    @JoinColumn
//    private Team team;
//
////    @OneToOne
////    @JoinColumn(name="LOCKER_ID")
////    private Locker locker;
////
////    @OneToMany(mappedBy = "member")
////    private List<MemberProduct> memberProducts = new ArrayList<>();
//
//
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//
//    public Team getTeam() {
//        return team;
//    }
//
//    public void setTeam(Team team) {
//        this.team = team;
//    }
//
////    public Locker getLocker() {
////        return locker;
////    }
////
////    public void setLocker(Locker locker) {
////        this.locker = locker;
////    }
////
////    public List<MemberProduct> getMemberProducts() {
////        return memberProducts;
////    }
////
////    public void setMemberProducts(List<MemberProduct> memberProducts) {
////        this.memberProducts = memberProducts;
////    }
}
