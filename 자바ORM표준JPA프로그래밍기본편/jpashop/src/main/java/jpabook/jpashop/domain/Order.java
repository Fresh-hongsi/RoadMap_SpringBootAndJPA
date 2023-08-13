package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ORDERS") //DB에서 ORDER가 예약어로 걸려있는 경우가 있으므로 이렇게 설정
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ORDER_ID")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
   private Member member;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) //주문을 생성할 때 delivery도 자동 persist
    @JoinColumn(name="DELIVERY_ID")
    private Delivery delivery;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL) //주문을 생성할때 orderItem도 자동 persist
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate; //주문한 시각

    @Enumerated(EnumType.STRING) //반드시 enum은 string상태로 해주기
    private OrderStatus status; //주문상태: 주문완료, 주문취소

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }



    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


}
