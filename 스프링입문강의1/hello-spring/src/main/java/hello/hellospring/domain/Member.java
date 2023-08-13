package hello.hellospring.domain;

import javax.persistence.*;

@Entity //이 어노테이션을 쓰면 Member 객체는 jpa가 관리하게 됨
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //디비가 알아서 pk에 해당하는 키값을 지정하는 기능
    private Long id; //회원의 id

    //@Column(name="username") //디비에 있는 username컬럼에 name값을 넣을 것
    private String name; //회원의 이름
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
}
