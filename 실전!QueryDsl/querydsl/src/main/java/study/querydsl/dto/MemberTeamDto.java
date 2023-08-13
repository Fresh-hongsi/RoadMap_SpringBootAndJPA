package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

//회원과 팀에 관한 attribute모두를 가져오는 dto
@Data
public class MemberTeamDto {

    private Long memberId;
    private String username;
    private int age;
    private Long teamId;
    private String teamName;


    @QueryProjection //이걸 놓고 main의 querydslApplication.java 실행하면 memberDto의 q파일이 만들어짐
    public MemberTeamDto(Long memberId, String username, int age, Long teamId, String teamName) {
        this.memberId = memberId;
        this.username = username;
        this.age = age;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
