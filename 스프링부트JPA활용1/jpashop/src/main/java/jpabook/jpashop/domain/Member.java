package jpabook.jpashop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

//엔티티의 식별자는 id 를 사용하고 PK 컬럼명은 member_id 를 사용했다.
//엔티티는 타입 (여기서는 Member )이 있으므로 id 필드만으로 쉽게 구분할 수 있다.
//테이블은 타입이 없으므로 구분이 어렵다. 그리고 테이블은 관례상 테이블명 + id 를 많이 사용한다.
//참고로 객체에서 id 대신에 memberId 를 사용해도된다. 중요한 것은 일관성이다
@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id; //em.persist한 순간 pk값 받는 것을 보장

    //@NotEmpty //validation위해 넣어줌 -> 이름값은 꼭 있어야 db에 저장할 수있어!
    private String name;

    //@JsonIgnore //이렇게 하면 멤버 엔티티를 반환해도 주문 내용이 json에는 빠지게 됨 -> 이 방식 좋지 않음. 엔티티에 화면에 띄우기 위한 로직이 추가되었으므로
    @Embedded //주소는 내장타입을 사용했음을 명시
    private Address address;


    @JsonIgnore //order에서 member를 찾는 건 ok. member에서 다시 order찾는건 json ignore해주기
    @OneToMany(mappedBy = "member") //member입장에서 주문은 여러개 매핑 가능, order table에 있는 member에 의해 mapping되었음을 명시. 연관관계의 거울, 조회기능만 가능
    private List<Order> orders = new ArrayList<>();
}
