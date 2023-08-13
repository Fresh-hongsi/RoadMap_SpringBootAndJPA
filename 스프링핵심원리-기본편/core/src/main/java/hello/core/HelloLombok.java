package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter //롬복 라이브러리를 활용한 게터
@Setter //롬복 라이브러리를 활용한 세터
@ToString //롬복 라이브러리를 활용한 스트링 생성기
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("asd");

//        String name= helloLombok.getName();
//        System.out.println("name = " + name);
        System.out.println("helloLombok = " + helloLombok); //helloLombok 객체를 알아서 string으로 변환해줌
    }
}
