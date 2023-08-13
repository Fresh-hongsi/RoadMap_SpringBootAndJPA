package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {

        List<OrderQueryDto> result = findOrders();

        //일단 orderItem 안들어있는 order를 찾아와서 여기서 다시 각각 order돌면서 orderItem세팅
        result.forEach(o-> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); //또다른 함수 실행하면서 쿼리 날림
            o.setOrderItems(orderItems); //여기서 직접 order에 OrderQueryDto리스트 넣어주기
        });

        return result;

    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders(); //여기까진 동일 (모든 order를 가져오는 것)
        //한방에 order에 대응하는 orderitem, item가져오기


        //order에 있는 orderid를 뽑아주기 그리고 아래에서 in쿼리 써서 성능 최적화
        //그러면 orderId에 매칭된 orderitem, item이 나오고, 그걸 dto로 변환해서 리스트로 만들어줌
        List<Long> orderIds = toOrderIds(result);
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        //주문에 orderitem세팅
        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        //그러면 orderId에 매칭된 orderitem, item이 나오고, 그걸 dto로 변환해서 리스트로 만들어줌
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        //위까지 실행되면 List<OrderItemQueryDto>는  orderid1 -jpa1 / orderid1 -jpa2 / orderid2-spring1 / orderid2-spring2
        //가 리스트에 들어있는데, 그걸 orderid기준으로 grouping해서 집합으로 바꿈
        //즉 order1-jpa1-jpa2 / order2-spring1-spring2 가 됨
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        //order에 있는 orderid를 뽑아주기 그리고 아래에서 in쿼리 써서 성능 최적화
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }

    //OrderQueryDto 내에서 쿼리 날려서 orderItem까지 세팅시키기
    //앞의 과정에서는 order를 찾아왔으므로 orderId가 존재할 것
    //그후 orderItems item 조인한 테이블에서 orderId와 일치하는 값이 있는 걸 찾은 후에 OrderItemQueryDto를 만들면서 세팅
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count)"+
                        " from OrderItem oi"+
                        " join oi.item i"+
                        " where oi.order.id =:orderId",OrderItemQueryDto.class)
                .setParameter("orderId",orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() { //orderItem은 조인 x
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }


    public List<OrderFlatDto> findAllByDto_flat() {

        //db에서 쿼리를 한번에 가져오기, 데이터 뻥튀기 일어날 것
        return em.createQuery("select new "+
                        " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id,m.name,o.orderDate,o.status,d.address,i.name,oi.orderPrice,oi.count)" +
                        "from Order o"+
                        " join o.member m"+
                        " join o.delivery d"+
                        " join o.orderItems oi"+
                        " join oi.item i",OrderFlatDto.class)
                .getResultList();
    }
}
