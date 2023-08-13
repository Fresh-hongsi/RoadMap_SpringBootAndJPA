package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//엔티티매니저를 만드는 공장 생성->얘는 app로딩 시점에 딱 하나만 만들어야함

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성 -> em은 트랜잭션 단위 내에서 사용

        //여기에 코드 들어감
        EntityTransaction tx = em.getTransaction(); //트랜잭션 얻어옴

        tx.begin();//트랜잭션 시작

        try{
            //jpa는 모든 데이터를 변경하는 단위가 트랜잭션 내에서 이뤄져야함
            //방금 만든 member entity 클래스를 이용해 db에 직접 member를 저장해보겠음
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("helloB");
//            em.persist(member);

            //수정하기
//            Member findMember = em.find(Member.class, 1L); //db에서 pk가 1인 member를 찾아와서 findeMember에 넣어줌
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());

//            //삭제하기
//            Member findMember = em.find(Member.class, 1L); //db에서 pk가 1인 member를 찾아와서 findeMember에 넣어줌]]
//            em.remove(findMember);

            //수정하기
//            Member findMember = em.find(Member.class, 1L); //db에서 pk가 1인 member를 찾아와서 findeMember에 넣어줌. findmember는 jpa가 관리하는 대상임
//            findMember.setName("helloJPA"); //트랜잭션 커밋시 jpa가 체크하고 있다가 업데이트 쿼리 날려줌
            //em.persist(findMember)할필요 없음->jpa가 관리하고 있으므로!: 트랜잭션 커밋시 jpa가 체크하고 있다가 업데이트 쿼리 날려줌

            //jpql활용
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    .setFirstResult(5) //페이지네이션 관련
//                    .setMaxResults(8) //페이지네이션 관련
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member.name = " + member.getName());
//            }

            //영속화하기
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("HelloJPA"); //여기까진 비영속 상태
//
//            System.out.println("=== before ===");
//            em.persist(member); //여기서 영속상태 , 이때 db에 저장되지 않음
//            //em.detach(member);//영속화 되어있던 애를 컨텍스트에서 지움
//            //em.remove(member); //실제 db에 있던 member를 지우겠어
//            System.out.println("=== after ===");

//            Member findMember1 = em.find(Member.class, 101L);//조회, db에서 조회
//            Member findMember2 = em.find(Member.class, 101L);//조회, 1차 캐시에서 조회
//            System.out.println("result = "+(findMember1==findMember2));
            //System.out.println("findMember.getId() = " + findMember.getId());
            //System.out.println("findMember.getName() = " + findMember.getName());

            //Member member1=new Member(15L,"c");
            //Member member2=new Member(16L,"d");

//            Member member = em.find(Member.class, 150L);
//            member.setName("zzz"); //jpa는 자바 컬렉션처럼, 엔티티 찾아와서 수정한 다음, 다시 업데이트하라는 명령이 없어도 된다. persist,update따로 하지 x
//            Member member = new Member(200L, "member200");
//            em.persist(member);
//            em.flush(); //강제로 db에 반영

//            Member member = em.find(Member.class,150L);
//            member.setName("aaaaa");
//            //em.detach(member); //영속성 컨텍스트에서 분리 -> 준영속상태 -> 방금 변경한게 의미가 없어짐
//            em.clear(); //영속성 컨텍스트 전체를 초기화 -> 이것도 이제 변경한게 의미가 없어짐
//
//            Member member2 = em.find(Member.class,150L);
//            System.out.println("----------------");
//            Member member = new Member();
//            //member.setId("ID_A");
//            member.setUsername("C");
//
//            System.out.println("===============");
//
//            em.persist(member); //전략이 identity인 경우, 울며 겨자먹기로 persist하자마자 insert쿼리 날리고, jpa가 영속성관리하기 위해 member의 key값을 들고 있음
//            System.out.println("member.getId() = " + member.getId());
//            System.out.println("===============");

//            Member member1 = new Member();
//            member1.setUsername("A");
//
//            Member member2 = new Member();
//            member2.setUsername("B");
//
//            Member member3= new Member();
//            member3.setUsername("C");
//
//            System.out.println("===============");
//
//            em.persist(member1); //1, 51
//            em.persist(member2); //MEM
//            em.persist(member3); //MEM
//
//            System.out.println("member1 = " + member1.getId());
//            System.out.println("member2 = " + member2.getId());
//            System.out.println("member3 = " + member3.getId());
//
//            System.out.println("===============");

            //저장하는 코드
//            Team team=new Team();
//            team.setName("TeamA");
//            //team.getMembers().add(member); //team은 연관관계의 거울이므로 읽기 전용이어야함
//            em.persist(team);
//
//            Member member = new Member();
//            member.setUsername("member1");
//            //member.changeTeam(team); //member가 연관관계의 주인이므로 이렇게 세팅해줘야함
//            em.persist(member);
//
//            //team.getMembers().add(member); //양쪽에 명시적으로 넣어줘야함! 1차 캐시 문제때문에
//            team.addMember(member);


//            em.flush();
//            em.clear();

//            Team findTeam = em.find(Team.class, team.getId());
//            List<Member> members = findTeam.getMembers();
//
//            System.out.println("================");
//            for (Member m : members) {
//                System.out.println("m.getUsername() = " + m.getUsername());
//            }
//            System.out.println("================");

//            Member member = new Member();
//            member.setUsername("member1");
//            em.persist(member);
//
//            Team team = new Team();
//            team.setName("teamA");
//            team.getMembers().add(member);
//
//            em.persist(team);

//            Movie movie = new Movie();
//            movie.setDirector("aaaa");
//            movie.setActor("bbbb");
//            movie.setName("바람과 함께 사라지다");
//            movie.setPrice(10000);
//            em.persist(movie);
//
//            em.flush(); //쌓여있던 쿼리 실행
//            em.clear(); //영속성 컨텍스트 비우기, 1차 캐시 비워짐
//
//            Movie findMovie = em.find(Movie.class, movie.getId());
//            System.out.println("findMovie = " + findMovie);

//            Member member = new Member();
//            member.setUsername("user1");
//            member.setCreatedBy("kim");
//            member.setCreatedDate(LocalDateTime.now());
//            em.persist(member);

//            Team team = new Team();
//            team.setName("teamA");
//            em.persist(team);
//
//            Member member1 = new Member();
//            member1.setUsername("member1");
//            member1.setTeam(team);
//            em.persist(member1);
//
//
//
//
//            em.flush();
//            em.clear();
//
//            Member m = em.find(Member.class, member1.getId());
////            Member findMember = em.find(Member.class, member.getId());
////            System.out.println("findMember = " + findMember.getClass());
////            System.out.println("findMember.id = " + findMember.getId());
////            System.out.println("findMember.username = " + findMember.getUsername());
//            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());
//
//            System.out.println("==============");
//            m.getTeam().getName();
//            System.out.println("==============");
//            Child child1 = new Child();
//            Child child2 = new Child();
//
//            Parent parent = new Parent();
//            parent.addChild(child1);
//            parent.addChild(child2);
//
//            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);
//
//            em.flush();
//            em.clear();
//
//            Parent findParent = em.find(Parent.class, parent.getId());
//            //findParent.getChildList().remove(0); //첫번째 자식 지우기
//            em.remove(findParent);

//            Address address = new Address("city","street","10000");
//
//            Member member = new Member();
//            member.setUsername("member1");
//            member.setHomeAddress(address);
//            em.persist(member);
//
//            Address newAddress = new Address("NewCity", address.getStreet(), address.getZipcode());
//            member.setHomeAddress(newAddress); //이런 식으로 하면 부작용 없이 임베디드 활용해 멤버의 주소 변경 가능

//            Member member = new Member();
//            member.setUsername("member1");
//            member.setHomeAddress(new Address("homeCity","street","10000")); //이건 멤버의 테이블에 들어감
//
//            member.getFavoriteFoods().add("치킨"); //값타입으로 매핑된 컬렉션이므로, member의 id값을 외래키로 이용해서 FACORITE_FOOD 테이블에 들어감
//            member.getFavoriteFoods().add("족발"); //값타입으로 매핑된 컬렉션이므로, member의 id값을 외래키로 이용해서 FACORITE_FOOD 테이블에 들어감
//            member.getFavoriteFoods().add("피자"); //값타입으로 매핑된 컬렉션이므로, member의 id값을 외래키로 이용해서 FACORITE_FOOD 테이블에 들어감
//
//            member.getAddressHistory().add(new AddressEntity("old1","street","10000"));
//            member.getAddressHistory().add(new AddressEntity("old2","street","10000"));
//
//            em.persist(member);
//
//            em.flush();
//            em.clear();

//            System.out.println("-------------start");
//            Member findMember = em.find(Member.class, member.getId()); //컬렉션 값 타입은 지연로딩되는 것을 알 수 있음
//
//            Address a= findMember.getHomeAddress();
//            findMember.setHomeAddress(new Address("NEWCITY",a.getStreet(),a.getZipcode()));
//
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");

//            findMember.getAddressHistory().remove(new AddressEntity("old1","street","10000")); //이러면 equals로 동작해서 해당 주소를 지워줌
//            findMember.getAddressHistory().add(new AddressEntity("old2","street","10000"));

//            List<Address> addressHistory = findMember.getAddressHistory();
//            for (Address address : addressHistory) {
//                System.out.println("address.getCity() = " + address.getCity());
//
//            }
//
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            for (String favoriteFood : favoriteFoods) {
//                System.out.println("favoriteFood = " + favoriteFood);
//            }

            List<Member> result = em.createQuery("select m From Member m where m.username like '%kim%'", Member.class) //Member entity에서 이름에 kim이 포함된 멤버 객체를 모두 가져와
                    .getResultList();

            for (Member member : result) {
                System.out.println("member = " + member);
            }


            tx.commit(); //성공: 트랜잭션 종료, 커밋 시점에 영속성 컨텍스트에 있는 애가 db에 쿼리 날라감
        }catch (Exception e)
        {
            tx.rollback(); //에러: 롤백하기
            e.printStackTrace();
        }finally { //작업 수행 다 끝나면
            em.close(); //엔티티 매니저 종료

        }

        emf.close(); //엔티티 매니저 공장 종료





    }

//    private static void printMember(Member member)
//    {
//        System.out.println("member = " + member.getUsername());
//    }
//     //찾아온 회원과 팀 정보를 출력하는 비즈니스 로직이 있다 치자
//    private static void printMemberAndTeam(Member member)
//    {
//        String username = member.getUsername();
//        System.out.println("username = " + username);
//
//        Team team = member.getTeam();
//        System.out.println("team = " + team.getName());
//    }
}
