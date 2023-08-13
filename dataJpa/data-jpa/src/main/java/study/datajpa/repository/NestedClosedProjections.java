package study.datajpa.repository;

public interface NestedClosedProjections {


    //data jpa - PROJECTIONS강의 13분정도에 이거 함
    //멤버의 이름과 팀의 이름만 프로젝션해서 갖고오고 싶은 상황


    String getUsername(); //중첩 아닌애는 딱 이름만 프로젝션해서  가져옴
    TeamInfo getTeam(); //중첩 쿼리인 애는 team전체를 가져오게됨

    interface TeamInfo {
        String getName(); //팀의 이름 필드만 프로젝션
    }


}
