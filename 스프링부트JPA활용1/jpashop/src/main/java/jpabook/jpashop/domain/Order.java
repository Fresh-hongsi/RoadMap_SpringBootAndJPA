package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//manytoone: default->eager
//onetomany: default->lazy
//onetoone: detault->eager
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //함부로 생성자 호출하지 못하게 막아주는 롬복 도구 -> 밑의 static 함수인 createOrder가 실질적으로 생성자 역할을 함
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //주문 입장에서 member은 한 명과만 묶일 수 있음
    @JoinColumn(name="member_id") //이렇게 하면 외래키인 member_id와 join ->order table이 member와의 연관관계에서 주인이 됨
    private Member member;

    //@BatchSize(size = 1000) //로컬하게 배치 사이즈 적용
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //order에 대해서 persist하면 order내의 orderitem에 대해서도 persist됨(cascade).원래대로라면 orderitem각각 먼저 persist한 후에 order도 persist해줘야했음
    private List<OrderItem> orderItems = new ArrayList<>();
//    컬렉션은 필드에서 초기화 하자.
//    컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.
//    null 문제에서 안전하다.
//    하이버네이트는 엔티티를 영속화 할 때, 컬랙션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다.
//    만약 getOrders() 처럼 임의의 메서드에서 컬력션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있다.
//    따라서 필드레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.
    //가급적 컬렉션은 딱 이렇게 세팅만하고, 컬렉션 내부를 조회 삭제 추가 정도만 하지, 컬렉션 설정을 바꾸진 말자(하이버네이트가 기껏 관리하고 있는데, 관리에 문제가 생길 수 있음)

    //order에 대해 persist하면 delivery에 대해서도 persist해줌(cascade)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //jpa에서는 1:1 관계 시 foreign key를 order에 넣어도 되고 delivery에 넣어도 됨, 근데 access가 많을 예정인 order에 delivery를 포린키로 설정할 것
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING)//디폴트가 ordinal->숫자로 컬럼에 들어감 -> string으로 변경(그래야 문자열로 타입 조회 가능)
    private OrderStatus status; //주문상태 enum [ORDER,CANCEL]

    //==연관관계 편의 메서드==// -> 위치는? 실질적으로 control하는 쪽에 갖고 있으면 좋음. member보다 order에 있는게 좋음
    public void setMember(Member member)
    {
        this.member=member; //이 주문에 대한 member저장
        member.getOrders().add(this); //member의 주문에 이 주문 추가
    }

    //==연관관계 편의 메서드==// -> 위치는? 실질적으로 control하는 쪽에 갖고 있으면 좋음. orderitem보다 order에 있는게 좋음
    public void addOrderItem(OrderItem orderItem)
    {
        orderItems.add(orderItem); //주문에 상품 추가
        orderItem.setOrder(this); //아이템에 해당 주문 정보 저장
    }

    //==연관관계 편의 메서드==// -> 위치는? 실질적으로 control하는 쪽에 갖고 있으면 좋음. delivery보다 order에 있는게 좋음
    public void setDelivery(Delivery delivery)
    {
        this.delivery=delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==/ -> order만 생성해서 될 게 아니라 orderItem도 들어가고 delivery도 들어가고 해서 복잡해짐
    public static Order CreateOrder(Member member, Delivery delivery, OrderItem... orderItems){ //OrderItem...: OrderItem여러개를 파라미터로 넘기겠다는 뜻
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem: orderItems)
        {
            order.addOrderItem(orderItem); //하나의 주문에 여러 item을 넣을 수 있으므로 이런 방식으로 order에 item을 넣어줌 -> ex:닭강정2개, 치즈스틱3개...
        }

        order.setStatus(OrderStatus.ORDER); //주문완료
        order.setOrderDate((LocalDateTime.now())); //주문 완료 시점을 현재 시간으로 세팅
        return order;
    }

    //==비즈니스 로직==//
    //주문취소
    public void cancel() {
        if(delivery.getStatus() ==DeliveryStatus.COMP ) //이미 배송 완료되었으면 주문취소 불가능한 비즈니스 로직
        {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL); //위의 예외를 통과했다면 주문 취소 가능

        for(OrderItem orderItem:orderItems){ //주문 취소했으니 재고 원상복구 시켜줘야함
            orderItem.cancel(); //주문 상품 각각에 취소 메서드 호출
        }
    }


    //==조회 로직==//
    //전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice=0;
        for(OrderItem orderItem:orderItems)
        {
            totalPrice+=orderItem.getTotalPrice();
        }

        return totalPrice;


    }
}
