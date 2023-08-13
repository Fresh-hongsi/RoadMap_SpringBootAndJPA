package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em; //스프링이 엔티티 매니저를 만들어서 주입해줌

    //상품 등록
    public void save(Item item)
    {
        if(item.getId()==null) //item은 jpa에 저장하기 전까지 id값이 없음
        {
            em.persist(item); //id값이 없다는건 persist통해 객체를 새로 생성해야함을 의미
        }
        else { //id값이 있다는 건 어디 db에 저장되어있는 item을 가져온다는 것 -> 병합은 준영속 상태의 엔티티를 영속 상태로 변경할때 사용하는 기술임
            em.merge(item); //트랜잭션 커밋시에 넘어온 item으로 다 어트리뷰트 바꿔치기하면서 영속화함
            //<merge>동작과정
//            1. merge() 를 실행한다.
//            2. 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회한다.
//            2-1. 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고, 1차 캐시에 저장한다.
//            3. 조회한 영속 엔티티( mergeMember )에 member 엔티티의 값을 채워 넣는다. (member 엔티티의 모든 값
//            을 mergeMember에 밀어 넣는다. 이때 mergeMember의 “회원1”이라는 이름이 “회원명변경”으로 바
//            뀐다.)
//            4. 영속 상태인 mergeMember를 반환한다.
//            주의: 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이
//            변경된다. 병합시 값이 없으면 null 로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체한다.)
            //가급적 merge쓰지말자
        }
    }

    //상품 하나 조회
    public Item findOne(Long id)
    {
        return em.find(Item.class,id);
    }

    //모든 상품 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
