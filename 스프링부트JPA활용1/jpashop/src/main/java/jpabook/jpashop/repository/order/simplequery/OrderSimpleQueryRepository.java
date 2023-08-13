package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        //Order, Member, Delivery를 조인한거에서 필요한 필드를 dto객체에 파라미터로 넣어서 dto 생성하고, 생성한 dto들을 list로 묶어서 반환!!!
        //v3보다 좋은 점은 select쿼리 나갈때 모든 거 조인하는게 아니라, 내가 필요한 dto의 필드만 join해서 가져옴
        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name,o.orderDate,o.status,d.address)  "+
                        "from Order o join o.member m join o.delivery d",OrderSimpleQueryDto.class)
                .getResultList();
    }
}
