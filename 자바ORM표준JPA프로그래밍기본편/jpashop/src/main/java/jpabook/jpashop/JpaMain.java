package jpabook.jpashop;

import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//엔티티매니저를 만드는 공장 생성->얘는 app로딩 시점에 딱 하나만 만들어야함

        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성 -> em은 트랜잭션 단위 내에서 사용

        //여기에 코드 들어감
        EntityTransaction tx = em.getTransaction(); //트랜잭션 얻어옴

        tx.begin();//트랜잭션 시작

        try{
            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");
            em.persist(book);

            tx.commit(); //성공: 트랜잭션 종료, 커밋 시점에 영속성 컨텍스트에 있는 애가 db에 쿼리 날라감
        }catch (Exception e)
        {
            tx.rollback(); //에러: 롤백하기
        }finally { //작업 수행 다 끝나면
            em.close(); //엔티티 매니저 종료

        }

        emf.close(); //엔티티 매니저 공장 종료





    }
}
