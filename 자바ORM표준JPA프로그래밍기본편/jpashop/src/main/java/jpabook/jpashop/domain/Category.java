package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY) //자식 입장에서 부모는 하나임. 셀프 매핑
    @JoinColumn(name="PARENT_ID")
    private Category parent;


    @OneToMany(mappedBy = "parent") //셀프 매핑. 원투매니로 여러명의 자식 매핑
    private List<Category> child = new ArrayList<>();

    @ManyToMany
    @JoinTable(name="CATEGORY_ITEM",joinColumns = @JoinColumn(name="CATEGORY_ID"),inverseJoinColumns = @JoinColumn(name="ITEM_ID"))
    private List<Item> items = new ArrayList<>();
}
