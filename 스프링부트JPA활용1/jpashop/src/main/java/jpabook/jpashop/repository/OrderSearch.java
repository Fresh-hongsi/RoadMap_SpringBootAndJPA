package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String memberName;//회원의 이름 -> 동적쿼리에서 WHERE문에 들어갈 얘
    private OrderStatus orderStatus; //주문 상태: ORDER, CANCEL


}
