package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) //주문을 하려면 회원을 식병해야하고, 상품을 식별해야하고, 개수를 알아야함
    {
        //받은 id값들로 회원, 상품을 식별해서 찾아옴

        //엔티티 조회
        Member member = memberRepository.findById(memberId).get(); //회원 id값으로 회원 찾기
        Item item = itemRepository.findOne(itemId); //아이템 id로 아이템 찾기

        //배송정보 생성 -> 회원 엔티티가 갖고 있는 주소로 지정시키기(비즈니스 룰)
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성 (createOrderItem이 static메서드라 아무데서나 부를 수 있는거라 생각하자!- 추측)
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성 (createOrder이 static메서드라 아무데서나 부를 수 있는거라 생각하자!- 추측)
        Order order = Order.CreateOrder(member, delivery, orderItem);

        //주문을 저장
        orderRepository.save(order); //order만 save해도 cascade때문에 del,orderitem이 자동 persist됨!!!!!(중요) -> 이미 order과 delivery, order과 orderitem은 연관관계 편의 메서드도 이미 정의 되었으므로
        return order.getId(); //주문의 pk값을 반환

    }

    //주문취소
    @Transactional
    public void cancelOrder(Long orderId)
    {
        Order order = orderRepository.findOne(orderId);//id값을 바탕으로 주문정보 확인
        order.cancel(); //주문 취소
    }
    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }
}
