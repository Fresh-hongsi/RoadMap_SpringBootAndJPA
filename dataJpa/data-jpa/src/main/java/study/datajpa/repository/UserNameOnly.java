package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

//엔티티 대신에 DTO를 편리하게 조회할 때 사용
//전체 엔티티가 아니라 만약 회원 이름만 딱 조회하고 싶으면? ->projection사용
public interface UserNameOnly {

    //@Value("#{target.username+' '+target.age}") //이러면 getUsername반환될 떄 target인 member의 username과 age를 합친 문자열을 String으로 만들어서 반환
    String getUsername(); //윗 줄이 없다면 close projection, 윗줄이 있다면 open projection이라고 함
}
