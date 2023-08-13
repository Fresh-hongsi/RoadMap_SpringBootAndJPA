package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseEntity{



    @Id @GeneratedValue
    @Column(name="TEAM_ID")
    private Long id;

    private String name;

    @OneToMany //Member와 연관관계를 맺은 team 애트리뷰트를 연관관계의 거울로 사용, mapped by 있는 쪽이 연관관계의 주인이 아님
    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<>(); //관례: arrayList를 초기화 세팅해줘야함

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }



}
