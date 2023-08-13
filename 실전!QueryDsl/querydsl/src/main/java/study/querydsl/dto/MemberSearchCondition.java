package study.querydsl.dto;

import lombok.Data;

@Data
public class MemberSearchCondition { //검색조건 - admin화면인데, 회원명, 팀이름, 회원 나이 조건을 가지고 검색할 수 있는 필터링 기능

    private String username; //회원의 이름 검색조건
    private String teamName; //팀 이름 검색조건
    private Integer ageGoe; //회원 나이 검색조건1
    private Integer ageLoe; //회원 나이 검색조건2
}
