package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;

    public UsernameOnlyDto(String username) { //생성자에 parameter명(username)으로 매칭시켜서 하는 프로젝션도 됨
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
