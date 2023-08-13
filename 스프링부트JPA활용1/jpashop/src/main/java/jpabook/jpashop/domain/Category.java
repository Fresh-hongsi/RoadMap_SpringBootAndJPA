package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name="category_item", joinColumns = @JoinColumn(name="category_id"), inverseJoinColumns = @JoinColumn(name="item_id"))  //다대다 관계에서는 joinTABLE필요 -> item과 category의 연관관계를 풀어낼 category_item table 만들기
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY) //카테고리의 계층 구조를 위해 필요 -> 내 부모와 나는 many to one관계임. 나랑 형제는 한 부모와 관계를 가짐
    @JoinColumn(name="parent_id") //나중에 이 부분 다시 이해해보자
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 편의 메서드==//
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }


}
