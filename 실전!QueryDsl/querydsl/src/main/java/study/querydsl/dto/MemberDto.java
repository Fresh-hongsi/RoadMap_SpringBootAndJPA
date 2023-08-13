package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data //기본생성자는 빠져있으므로 따로 만들어줘야함. @NoArgsConstructor넣어줘도됨
@NoArgsConstructor
@Data
public class MemberDto {
    private String username;
    private int age;

    //여기에 삽입 - > 섹션 4의 @QueryProjection관련
    @QueryProjection //이걸 놓고 main의 querydslApplication.java 실행하면 memberDto의 q파일이 만들어짐
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }


    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
