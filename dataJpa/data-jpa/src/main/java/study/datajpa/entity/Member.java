package study.datajpa.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //jpa는 디폴트 생성자가 필요한데, 이를 명시적으로 적어준다. 이렇게 하면 디폴트 생성자 따로 표기 안해줘도 됨
@ToString(of = {"id", "username","age"}) //객체 출력할 떄 사용, //team도 넣으면 무한루프 발생! 양방향 연관관계인 필드를 넣으면 안된다
@NamedQuery(
        name="Member.findByUsername",
        query = "select m from Member m  where m.username =:username"
)
@NamedEntityGraph(name="Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String username;

    private int age;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    //jpa이기 떄문에 기본 생성자도 하나 있어야 함 (엔티티는 디폴트 생성자가 하나씩은 있어야함. 근데 아무데서나 호출되지 않도록 프로텍티드 사용-> 프록시 문제 떄문에!)
//    protected Member() {
//    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username= username;
        this.age=age;
        if(team!=null)
        {
            changeTeam(team); //연관관계 편의 메서드 적용
        }

    }

    //연관관계 편의 메서드
    public void changeTeam(Team team)
    {
        this.team=team; //이 회원의 팀 필드 변경
        team.getMembers().add(this); //바뀐 팀에 이 회원 저장
    }
}
