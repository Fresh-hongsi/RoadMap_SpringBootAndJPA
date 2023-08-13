package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //함부로 생성자 호출하지 못하게 막아주는 롬복 도구
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @JsonIgnore //order에서 orderitem를 찾는 건 ok. orderitem에서 다시 order찾는건 json ignore해주기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; //주문 가격

    private int count; //주문 수량

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count)
    {
        OrderItem orderItem=new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count );//기본적으로 주문 들어오자마자 item의 재고를 까줘야함
        return orderItem;
    }
    //==비즈니스 로직==//
    //주문 취소에 의한 주문 상품 원상복구
    public void cancel() {
        getItem().addStock(count); //상품의 주문 갯수만큼 item재고 수를 늘려줘야힘
    }

    //==조회 로직==//
    //주문 상품 전체 가격 조회
    public int getTotalPrice(){
        return getOrderPrice()*getCount();
    }
}
