package jpabook.jpashop.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){ //주문을 레포지토리에 저장
        em.persist(order);
    }

    //주문 단건 조회
    public Order findOne(Long id)
    {
        return em.find(Order.class,id);
    }

//    //검색 필터에 의한 조회
//    public List<Order> findAll(OrderSearch orderSearch) {
//
//        return em.createQuery("select o from Order o join o.member m" + //기본적으로 order을 조회하고, order과 연관된 member랑 join해라
//                                " where o.status = :status " + //추가조건1: 상태가 parameter로 들어온 상태와 같고
//                                " and m.name like :name", //추가조건2: 이름이 parameter로 들어온 이름과 같은 order를 조회해라
//                                Order.class)
//                                .setParameter("status",orderSearch.getOrderStatus())
//                                .setParameter("name",orderSearch.getMemberName())
//                                .setMaxResults(1000) //결과를 최대 1000개로 제한
//                                .getResultList();
//
//
//    }

    //jpql
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    //jpa criteria
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대  1000건
        return query.getResultList();
    }


    public List<Order> findAllWithMemberDelivery() {
        //Order 가져올 떄 Order의 alias인 o와 관련된 member과 delivery까지 가져오는 한밤 쿼리
        //프록시도 아닌 진짜 객체를 가져옴 (fetch join에서)
        //v3.1에서 사용, 페이징까지 적용함
        //Order기준 toOne관계 걸려있는 애들은 fetch join
        //Order 기준 toMany관계인 얘 + toMany관계에 걸려있는 애와 또 걸려있는 애들은 join fetch x, 지연로딩으로 하고, @batchSize로 lazy최적화하기
        return em.createQuery("select o from Order o "+
                              "join fetch o.member m "+
                              "join fetch o.delivery d "
                              ,Order.class)
                .getResultList();
    }

//    querydsl이용
    //모든 고객의 주문 내용 반환
    public List<Order> findAll(OrderSearch orderSearch) {

        JPAQueryFactory query = new JPAQueryFactory(em);
        QOrder order = QOrder.order; //order 테이블
        QMember member = QMember.member; //member 테이블




        return query
                .select(order)
                .from(order)
                .join(order.member, member) //order의 member과 member 테이블 조인
                .where(statusEq(orderSearch.getOrderStatus()),nameLike(orderSearch.getMemberName())) //조건 넣어주기
                .limit(1000)
                .fetch();

    }

    private BooleanExpression nameLike(String memberName)
    {
        if(!StringUtils.hasText(memberName))
        {
            return null;
        }
        return QMember.member.name.like(memberName);
    }


    private BooleanExpression statusEq(OrderStatus statusCond)
    {
        if(statusCond==null) //주문 상태가 따로 입력 안받았다면 null반환
        {
            return null;
        }
        //주문 상태가 있다면 주문에서 입력받은 상태와 같은 order을 반환
        return QOrder.order.status.eq(statusCond);
    }

    //offset,limit받을 수 있게끔 별도로 또 하나 만들었음
    public List<Order> findAllWithMemberDelivery(int offset,int limit) {
        //Order 가져올 떄 Order의 alias인 o와 관련된 member과 delivery까지 가져오는 한밤 쿼리
        //프록시도 아닌 진짜 객체를 가져옴 (fetch join에서)
        //v3.1에서 사용, 페이징까지 적용함
        //Order기준 toOne관계 걸려있는 애들은 fetch join
        //Order 기준 toMany관계인 얘 + toMany관계에 걸려있는 애와 또 걸려있는 애들은 join fetch x, 지연로딩으로 하고, @batchSize로 lazy최적화하기
        //default_batch_fetch_size: 100 를 yaml에 추가(글로벌하게 적용)
        return em.createQuery("select o from Order o "+
                                "join fetch o.member m "+
                                "join fetch o.delivery d "
                        ,Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    public List<Order> findAllWithItem() { //이 쿼리는 주문 1개에 대응하는 item이 2개라 join되면 같은 주문에 대한 결과값이 두개가 나옴
        //즉 2개의 order이 나와야하는데, 4개의 order가 반환되어버림. 즉 데이터 뻥튀기가 됨
        //따라서 중복된 데이터를 없애줘야함 distinct 이용
        //jpa 기본편 페치조인1-기본 19분 참조
        //order은 id가 4인 컬럼에 대해 같은 주소값으로 참조하고 있음
        //jpql에 distinct 넣으면 db입장을 넘어서 같은 주소값을 참조하고 있는 order의 중복을 없애주는 효과까지 줌

        return em.createQuery(
                "select distinct o from Order o " +
                        "join fetch o.member m "+
                        "join fetch o.delivery d "+
                        "join fetch o.orderItems oi "+
                        "join fetch oi.item i",Order.class)
                .getResultList();


    }
}
