package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> { //DATA JPA는 SAVE호출할 떄 보통 GENERATEVALUE방식으로 동작할 걸 기대하고, Id값이 널이면 persist, 있으면 merge를 호출한다
    //만약 generatevalue 기능을 안쓰고 pk를 생성자 통해 직접 만드는 경우는 data jpa의 save를 호출할 떄 id값이 널이 아니므로 merge를 호출하게 된다.
    //이럴 경우는 Persistable 인터페이스를 구현해서 판단 로직 변경해야한다.
//    참고: JPA 식별자 생성 전략이 @GenerateValue 면 save() 호출 시점에 식별자가 없으므로 새로운 엔티티로 인식해서 정상 동작한다.
//    그런데 JPA 식별자 생성 전략이 @Id 만 사용해서 직접 할당이면 이미 식별자값이 있는 상태로 save() 를 호출한다.
//    따라서 이 경우 merge() 가 호출된다. merge() 는 우선 DB를 호출해서 값을 확인하고, DB에 값이 없으면 새로운 엔티티로 인지하므로 매우 비효율 적이다.
//    따라서 Persistable 를 사용해서 새로운 엔티티 확인 여부를 직접 구현하게는 효과적이다.
//    > 참고로 등록시간( @CreatedDate )을 조합해서 사용하면 이 필드로 새로운 엔티티 여부를 편리하게 확인할수 있다.
//    (@CreatedDate에 값이 없으면 새로운 엔티티로 판단)

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;



    public Item(String id) {
        this.id = id;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate==null; //createdDate가 null이면 새로운 객체, 있으면 이미 있는 객체임을 판단할 수 있음
    }
}
