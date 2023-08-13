package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@BatchSize(size=100) //toOne관계에서는 배치 사이즈를 엔티티 단위로 입력
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //ITEM의 상속관계에 있는 책, 영화, 앨범을 싱글테이블 전략으로 관리
@DiscriminatorColumn(name="dtype") //하위 세개의 자식 테이블을 관리하기 위한 구별자 삽입
@Getter
@Setter
public abstract class Item { //하위 상속관계 있으므로 추상 클래스로 선언

    @Id
    @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name; //공통속성
    private int price; //공통속성
    private int stockQuantity; //공통속성

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //--상품 엔티티에 대한 비즈니스 로직 추가==//
    //상품 재고 관리는 객체지향적으로 엔티티 내에서 해결하는게 맞음

    public void addStock(int quantity) //재고수량 증가하는 로직
    {
        this.stockQuantity+=quantity;
    }

    public void removeStock(int quantity) //재고 수량 감소시키는 로직
    {
        int restStock = this.stockQuantity-quantity;

        if(restStock<0)
        {
            throw new NotEnoughStockException("need more stock"); //만약 재고수량보다 더 많은 걸 주문하면 예외 터뜨림
        }
        this.stockQuantity=restStock;
    }


}
