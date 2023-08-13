package jpabook.jpashop.controller;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message ="회원 이름은 필수 입니다.") //회원 이름은 무조건 입력하게 하는 강제성 부여
    private String name;

    private String city;

    private String street;

    private String zipcode;
}
