package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//주문과 관련된 가장 simple한 api
//xtoone(ManyToOne, OneToOne)관계에서 api성능 최적화하는 방법에 대해 다룰것 -> XtoMany는 컬렉션인데, 이는 다음 챕터에서 다룰 것
//Order
//Order ->Member : manytoone
//Order -> Delivery : onetoone
//에만 연관관계가 걸리도록 할 것


@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    //json을 만들기 위해 order에 갔더니 member가 있고, member에 갔더니 order컬렉션이 있고... 무한루프 발생(양방향 연관관계여서 문제)
    //따라서 orderitem, delivery, member엔티티의 order관련 필드는 jsonignore처리했음
    //hibernate5module은 지연로딩 세팅된 order의 orderitem, delivery, member 관련 필드는 무시하고 null로 반환해버림
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());//생짜로 모든 order가져오기(orderSearch에 아무것도 안들어가잇으므로)
        for (Order order : all) {
            order.getMember().getName(); //lazy가 강제 초기화돼서 프록시인 member의 getname하는데, 제대로 된 값 가져옴
            order.getDelivery().getAddress(); //lazy가 강제 초기화돼서 프록시인 delivery의 getAddress하는데, 제대로 된 값 가져옴
        }

        return all;

    }

    //간단한 주문 조회 v2: 엔티티를 dto로 변환
    //예제여서 list로 반환하는데, 실제 작업할 떈 result객체로 한번 감싸서 json형식에 맞게 해서 반환해야함
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //Order 2개
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream() //모든 주문에 대해
                .map(o -> new SimpleOrderDto(o)) //하나의 주문을 찾아서 SimpleOrderDto의 생성자로 넣어줌
                .collect(Collectors.toList());//생성된 SimpleOrderDto들을 리스트로 만들어줌

        return result;
    }

    //간단한 주문 조회 v3: fetch join이용
    //기본으로  lazy로 설계 짜고, 묶어서 조회하고 싶을 떄는 fetch join기능 넣은 jpql쓰는게 굿임
    //엔티티를 조회하고, 그 후 엔티티를 dto로 변환하는 방식임
    //재사용성 좋음 -> 우리는 v3방식 쓰는게 좋을듯
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(); //여기서 이미 다 join해서 필요한 필드 다 갖고옴
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    //간단한 주문 조회 v4: jpa에서 바로 dto로 꺼내기(더 성능 최적화)
    //재사용성이 별로.. trade off
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data //api통해 반환될 json의 형식 attribute들을 dto로 세팅
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){ //주문을 통해 dto 생성
            orderId=order.getId();
            name=order.getMember().getName(); //lazy에 걸려잇던 쿼리 날라감(lazy 강제 초기화)
            orderDate=order.getOrderDate();
            orderStatus=order.getStatus();
            address=order.getDelivery().getAddress(); //lazy에 걸려잇던 쿼리 날라감 (lazy 강제 초기화)
        }

    }

}
