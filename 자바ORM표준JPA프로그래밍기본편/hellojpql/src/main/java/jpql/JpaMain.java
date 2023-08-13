package jpql;

import javax.persistence.*;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.List;


public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//엔티티매니저를 만드는 공장 생성->얘는 app로딩 시점에 딱 하나만 만들어야함

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성 -> em은 트랜잭션 단위 내에서 사용

        //여기에 코드 들어감
        EntityTransaction tx = em.getTransaction(); //트랜잭션 얻어옴

        tx.begin();//트랜잭션 시작

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);



//            em.flush();
//            em.clear();

            //쿼리 나가면 flush는 자동호출됨, 역속성 컨텍스트는 그대로 남아있음 -> 따라서 아래 쿼리 나가면 db랑 영속성 컨텍스트랑 싱크 안맞음
            //모든 회원의 나이를 20살로 바꿔라는 벌크 연산
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate(); //resultCount: 변경된 row의 개수

            //여기서 em.clear하면 이제 아래에서 가져오면 싱크 맞음
            em.clear();
            List<Member> findMembers = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            for (Member findMember : findMembers) {

                System.out.println("findMember = " + findMember);
            }

//            List<Member> resultList = em.createNamedQuery("Member.findByUserName", Member.class)
//                    .setParameter("username", "회원1")
//                    .getResultList();
//            for (Member member : resultList) {
//                System.out.println("member = " + member);
//            }


//            String query= "select "+
//                    "       case when m.age<=10 then '학생요금'"+
//                    "           when m.age>=60 then '경로요금' "+
//                    "           else '일반요금'"+
//                    "end "+
//                    "from Member m";

            //String query = "select coalesce(m.username,'이름없는 회원') from  Member m"; //username이 없으면 오른쪽 문자열 반환
            //String query = "select nullif(m.username,'관리자') from  Member m"; //username과 관리자라는 문자열이 같으면 null, 다르면 username반환
//            String query = "select m from Member m where m.team = :team ";
//            List<Member> members = em.createQuery(query, Member.class)
//                    .setParameter("team",teamA)
//                    .getResultList();
//            for (Member member : members) {
//                System.out.println("member = " + member);
//            }
//            //System.out.println("findMember = " + findMember);
//            for (Team team : result) {
//                System.out.println("team = " +team.getName()+", "+team.getMembers().size());
//                for(Member member : team.getMembers())
//                {
//                    System.out.println("member = " + member);
//                }
//            }



//            String query = "select m.username, 'HELLO', TRUE From Member m "+
//                    "where m.type = jpql.MemberType.USER";
//
//            List<Object[]> result = em.createQuery(query)//페이징 시에는 order by가 무조건 들어가있어야함!!!
//                    .getResultList();
//
//            for (Object[] objects : result) {
//                System.out.println("objects = " + objects[0]);
//                System.out.println("objects = " + objects[1]);
//                System.out.println("objects = " + objects[2]);
//            }
//            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)// dto 클래스를 만들고, 생성자 통해 찾은 username,age를 세팅
//                    .getResultList();
//
//            MemberDTO memberDTO = result.get(0);
//            System.out.println("memberDTO.getname = " + memberDTO.getUsername());
//            System.out.println("memberDTO.getage = " + memberDTO.getAge());

//
////            Object o= resultList.get(0); //첫번째 member의 id, age가져오기
////            Object[] result = (Object[]) o; //타입 캐스팅
//            System.out.println("username = "+result[0]);
//            System.out.println("age = "+result[1]);
            //List<Member> result = (List<Member>) resultList; //이러면 result는 영속성 컨텍스트에 의해 관리가 되고 있는 것인가?

            //Member findMember = result.get(0);
            //findMember.setAge(20);


//            Member result = query.getSingleResult(); //하나만 가져오면 이 명령어 쓰면 됨 ->하나라도 없으면 예외 반환됨!!!!
//            System.out.println("result = " + result);
////            List<Member> resultList = query.getResultList(); ->여러개 가져오면 이렇게 반복문
////            for (Member member1 : resultList) {
////                System.out.println("member1 = " + member1);
////            }
//
//
//            TypedQuery<String> query2 = em.createQuery("select m.username from Member as m", String.class); //타입 정보 명확: 반환형 -> typed query
//            Query query3 = em.createQuery("select m.username, m.age from Member as m");//타입 정보 명확 x : 반환형-> query



            tx.commit(); //성공: 트랜잭션 종료, 커밋 시점에 영속성 컨텍스트에 있는 애가 db에 쿼리 날라감
        } catch (Exception e) {
            tx.rollback(); //에러: 롤백하기
            e.printStackTrace();
        } finally { //작업 수행 다 끝나면
            em.close(); //엔티티 매니저 종료

        }

        emf.close(); //엔티티 매니저 공장 종료


    }
}
