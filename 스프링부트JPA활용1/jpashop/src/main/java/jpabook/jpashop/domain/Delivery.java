package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    //order에서 delivery를 찾는 건 ok. delivery에서 다시 order찾는건 json ignore해주기
    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)//디폴트가 ordinal->숫자로 컬럼에 들어감 -> string으로 변경(그래야 문자열로 타입 조회 가능)
    private DeliveryStatus status; //READY(배송준비), COMP(배송중)
}
