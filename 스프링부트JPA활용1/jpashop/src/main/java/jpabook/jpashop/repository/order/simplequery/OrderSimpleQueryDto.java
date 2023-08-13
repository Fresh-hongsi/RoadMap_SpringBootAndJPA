package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data //api통해 반환될 json의 형식 attribute들을 dto로 세팅
public class OrderSimpleQueryDto {


        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address){ //주문을 통해 dto 생성
            this.orderId=orderId;
            this.name= name;
            this.orderDate= orderDate;
            this.orderStatus=orderStatus;
            this.address=address;
        }


}
