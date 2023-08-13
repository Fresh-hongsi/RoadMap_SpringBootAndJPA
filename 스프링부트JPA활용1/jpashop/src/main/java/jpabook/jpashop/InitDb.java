package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 * userA가 해당 책 2개를 한번에 주문
 *  JPA1 BOOK
 *  JPA2 BOOK
 *
 * userB
 * userB가 해당 책 2개를 한번에 주문
 *  SPRING1 BOOK
 *  SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct //스프링이 다 뜨고 나서, postconstructor가 실행돼서 메서드 진행
    //@PostConstruct는 Spring Framework에서 제공하는 애노테이션 중 하나로, 해당 메서드를 통해 초기화 작업을 수행할 수 있도록 지원합니다. 이 애노테이션을 사용하면 Spring Bean이 생성된 후에 자동으로 호출되는 초기화 메서드를 지정할 수 있습니다.
    //@PostConstruct를 붙인 메서드는 다음과 같은 특징을 가지고 있습니다:
    //메서드 시그니처: @PostConstruct를 붙인 메서드는 매개변수가 없어야 합니다. 반환 타입이 void이어야 합니다.
    //순서: Spring Bean의 라이프사이클 중 @PostConstruct 메서드는 객체 생성 후 초기화 단계에서 호출됩니다. 따라서 생성자가 실행된 후에 호출되며, 주입된 의존성들이 준비된 상태에서 실행됩니다.
    //다양한 용도: 주로 Spring Bean의 초기화 작업을 수행하는 용도로 사용됩니다. 예를 들어, 데이터베이스 연결, 파일 로딩, 설정 값 초기화 등의 작업을 수행할 수 있습니다.
    public void init() {
        initService.dbInit1(); //user1에 대한 db내용 반영 세팅
        initService.dbInit2(); //user2에 대한 db내용 반영 세팅
    }

    @Component //spring Boot의 컴포넌트 스캔은 기본적으로 클래스 내부에 있는 static으로 선언된 클래스를 스캔 대상에서 제외합니다.
    // static으로 선언된 클래스는 주로 외부 클래스의 도우미 클래스, 내부적인 상수 또는 유틸리티 클래스 등으로 사용되며, Spring Bean으로 등록되지 않습니다.
    //따라서 명시적으로 넣어주기
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        //엔티티 매니저로 바로 샘플 데이터 넣기
        private final EntityManager em;
        public void dbInit1(){
            System.out.println("Init1" + this.getClass());
            Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);//인터넷 안쓰고 여기서 바로 db에 접근하므로 가격도 내가 책 가격도 직접 세팅해야함
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);//인터넷 안쓰고 여기서 바로 db에 접근하므로 가격도 내가 책 가격도 직접 세팅해야함
            Delivery delivery = createDelivery(member);

            Order order = Order.CreateOrder(member, delivery, orderItem1, orderItem2);// order엔티티의 createOrder에서 ...의 비밀!! 이렇게 하면 여러 아이템을 한 주문에 받을 수 있음
            em.persist(order); //cascade 옵션 때문에 delivery랑 orderitem은 따로 persist안해도 order 이 persist될떄 같이 persist됨

        }

        public void dbInit2(){
            Member member = createMember("userB", "진주", "2", "2222");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);//인터넷 안쓰고 여기서 바로 db에 접근하므로 가격도 내가 책 가격도 직접 세팅해야함
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);//인터넷 안쓰고 여기서 바로 db에 접근하므로 가격도 내가 책 가격도 직접 세팅해야함
            Delivery delivery = createDelivery(member);


            Order order = Order.CreateOrder(member, delivery, orderItem1, orderItem2);// order엔티티의 createOrder에서 ...의 비밀!! 이렇게 하면 여러 아이템을 한 주문에 받을 수 있음
            em.persist(order); //cascade 옵션 때문에 delivery랑 orderitem은 따로 persist안해도 order 이 persist될떄 같이 persist됨

        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }
}


