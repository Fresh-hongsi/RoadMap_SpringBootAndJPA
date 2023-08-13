package study.querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) //team을 tostring하면 무한루프 빠질 수 있으므로 뺴놓는다!
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") //외래키
    private Team team;

    public Member(String username){
        this(username,0); //위의 username만 들어오는 생성자 호출이 있다면 username,0,null을 받는 밑의 생성자를 또 호출
    }

    public Member(String username, int age) {
        this(username,age,null); //위의 username,age만 들어오는 생성자 호출이 있다면 username,age,null을 받는 밑의 생성자를 또 호출
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team !=null)
        {
            changeTeam(team);
        }

    }

    //연관관계 편의 메서드
    public void changeTeam(Team team) {
        this.team=team;
        team.getMembers().add(this);
    }
}
