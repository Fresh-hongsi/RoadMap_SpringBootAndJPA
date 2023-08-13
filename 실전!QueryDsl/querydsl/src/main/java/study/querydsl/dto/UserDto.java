package study.querydsl.dto;

import lombok.Data;

@Data
public class UserDto {

    private String name; //memberDto랑 다르게 필드 이름이 name임!!!
    private int age;

    public UserDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public UserDto() {
    }
}
