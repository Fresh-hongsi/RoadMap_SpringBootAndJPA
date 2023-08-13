package jpabook.jpashop.api;
//    주문내역에서 추가로 주문한 상품 정보를 추가로 조회하자.
//    Order 기준으로 컬렉션인 OrderItem 와 Item 이 필요하다.
//    앞의 예제에서는 toOne(OneToOne, ManyToOne) 관계만 있었다.
//    이번에는 컬렉션인 일대다 관계(OneToMany)를 조회하고, 최적화하는 방법을 알아보자
// Order기준으로 OrderItem은 onetoMany관계, OrderItem과 Item은 manytoone관계.
// 앞서 만든 주문내역 조회에 order과 orderItem, orderItem과 iTEM을 엮어서 더 큰 주문조회 API를 다룰것

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    //orderItem까지 포함된 주문조회  - 엔티티 직접노출v1
    @GetMapping("api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());//검색 조건 없이 다 가져오기
        for (Order order : all) {
            order.getMember().getName(); //lazy가 강제 초기화돼서 프록시인 member의 getname하는데, 제대로 된 값 가져옴
            order.getDelivery().getAddress();//lazy가 강제 초기화돼서 프록시인 delivery의 getAddress하는데, 제대로 된 값 가져옴

            //핵심 -> order과 1대다로 묶인 orderItem 컬렉션을 가져와서 다시 iter돌려 각 item의 이름을 lazy 강제 초기화해서 item의 getName하는데, 제대로 된 값 가져옴
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());

            //위 람다식과 같은 코드
//            for (OrderItem orderItem : orderItems) {
//                orderItem.getItem().getName();
            //}
        }

        return all;


    }


    //orderItem까지 포함된 주문조회 - dto로 변환 v2
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){

        List<Order> orders = orderRepository.findAllByString(new OrderSearch()); //검색 조건 없이 다 가져오기
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o)) //하나의 주문을 찾아서 OrderDto의 생성자로 넣어줌
                .collect(toList());//생성된 OrderDto들을 리스트로 만들어줌
        return result;
    }

    //orderItem까지 포함된 주문조회 - fetch join으로 쿼리 수 줄이기 v3
    //딘점 : 1:다를 페치조인하는 순간 페이징 쿼리가 나가지 않는다는 점을 기억하자
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem(); //fetch join 쿼리 실행
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o)) //하나의 주문을 찾아서 OrderDto의 생성자로 넣어줌
                .collect(toList());//생성된 OrderDto들을 리스트로 만들어줌
        return result;
    }

    //orderItem까지 포함된 주문조회 - fetch join변경 + @Batchsize로 페이징까지 가능하게 하기 v3.1
    //http://localhost:8080/api/v3.1/orders?offset=1&limit=100
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value="offset",defaultValue = "0") int offset,
            @RequestParam(value="limit",defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit); //fetch join 쿼리 실행

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o)) //하나의 주문을 찾아서 OrderDto의 생성자로 넣어줌
                .collect(toList());//생성된 OrderDto들을 리스트로 만들어줌
        return result;
    }

    //orderItem까지 포함된 주문조회 - jpa에서 dto를 직접 조회하기 v4 -> n+1분제 발생.. 더 최적화 필요
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    //orderItem까지 포함된 주문조회 - jpa에서 dto를 직접 조회하기 v5 -> 컬렉션 조회 최적화
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    //orderItem까지 포함된 주문조회 - jpa에서 dto를 직접 조회하기 v6 -> 플랫 데이터 최적화
    //결과적으로 비추하는 방식
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> orderV6() {

        //일단 join해서 OrderFlatDto로 변환해서 가져왔는데, 뻥튀기 된 값도 있고, 우리가 원하는 api스펙인 OrderQueryDto랑 맞지 않음
        //따라서 노가다로 강제 변환 필요
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        //flat을 가지고 orderquerydto로 변환하는 과정
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }

    //v2와 관련된 dto
    @Getter
    static class OrderDto{

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        //새로 추가된 어트리뷰트 -> orderItem 컬렉션
        //private List<OrderItem> orderItems; //dto 내의 어트리뷰트에 OrderItem엔티티가 노출됨!!! 단점
        private List<OrderItemDto> orderItems; //이렇게 하면 외부에 orderItemDto로 랩핑되어 나가므로 안전


        public OrderDto(Order order) { //주문을 통해 dto 생성
            orderId=order.getId();
            name=order.getMember().getName(); //lazy에 걸려잇던 쿼리 날라감(lazy 강제 초기화)
            orderDate=order.getOrderDate();
            orderStatus=order.getStatus();
            address=order.getDelivery().getAddress(); //lazy에 걸려잇던 쿼리 날라감(lazy 강제 초기화)
//            order.getOrderItems().stream().forEach(o->o.getItem().getName()); //lazy에 걸려잇던 쿼리 날라감(lazy 강제 초기화)
////            getItem().getName()은 name 속성만을 가져오기 위함이 아닙니다.
////            'fetch = LAZY'로 인해 프록시 객체가 들어있는 "Item객체"를 가져오기 위함입니다.
////            getItem().getName()을 한다는 것의 의미는 "Item을 사용해야 한다는 것" 입니다.
////            Item을 사용해야하니 그때 지연로딩이 활성화되어 Item객체를 가져오는 것입니다.
////            name외에 다른 속성들도 조회가 가능한 것은 getName()을 사용해서 "Item 객체"를 조회했기 때문입니다.
////            Item객체를 가지고 있으니 당연히 그 안의 속성들도 알 수가 있는 거죠.
////            즉, 다시말해서.. 굳이 getName()이 아니라 다른 getXXX()를 사용해도 Item 객체를 조회해오는 사실은 변함이 없으니 똑같은 결과를 보실 수 있을 거예요.
//            orderItems=order.getOrderItems();

            //orderItem들에 대해서도 각각 dto로 변환하도록 함
            orderItems =order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }


    //dto 내의 어트리뷰트에 OrderItem엔티티가 노출됨!!! 단점을 해결하기 위해 OrderItem도 Dto로 바꿔줘야함
    @Getter
    static class OrderItemDto {

        //orderItem에 대해 노출하고 싶은 필드만 만들기
        private String itemName; //item엔티티에 들어있음
        private int orderPrice;  //orderItem에 들어있음
        private int count; //orderItem에 들어있음
        public OrderItemDto(OrderItem orderItem) {

            itemName=orderItem.getItem().getName();  //lazy에 걸려잇던 쿼리 날라감(lazy 강제 초기화)
            orderPrice=orderItem.getOrderPrice();
            count=orderItem.getCount();

        }
    }





}
