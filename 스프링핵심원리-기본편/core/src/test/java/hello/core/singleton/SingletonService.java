package hello.core.singleton;

public class SingletonService {

    //singleton 적용 예제
    //자기 자신의 생성자를 private로 클래스 내부에 갖고 있음
    //static으로 하면 클래스 레벨에 하나만 존재할 수 있게 됨



    //1. static 영역에 객체를 딱 1개만 생성해둔다.
    //프로그램 시작 시 static영억에 instance 딱 한 개 할당
    private static final SingletonService instance = new SingletonService();



    //2. public으로 열어서 객체 인스턴스가 필요하면 이 static 메서드를 통해서만 조회하도록 허용한다.
    //instance를 꺼낼 수 있는건 이 함수 호출 통해서만 가능
    public static SingletonService getInstance() {
        return instance;
    }

    //3. 생성자를 private으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 못하게 막는다.
    private SingletonService() {
    }


    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }


}
