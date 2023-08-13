package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BookForm {

    //아이템 공통 속성

    private Long id; //상품 수정을 위해 필요한 id필드
    private String name;
    private int price;
    private int stockQuantity;

    //책과 관련된 속성
    private String author;
    private String isbn;
}
