package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;


@Embeddable //주소는 내장타입 사용
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    //스프링 부트 관례상 값 타입 ㅋ클래스는 기본 생성자 넣어줘야함, 근데 protected로 해둬야 아무데서나 생성하지 않게 됨
    protected Address() {

    }

    //값 타입은 immutable하게 설계해야함 -> 변경이 되면 안되고, 생성될때만 딱 한번 세팅할 수 있도록 하는게 좋음
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
