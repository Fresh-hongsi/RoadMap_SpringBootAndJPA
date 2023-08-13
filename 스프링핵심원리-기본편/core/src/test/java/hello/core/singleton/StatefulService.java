package hello.core.singleton;

public class StatefulService {

    //클라이언트의 의도: 주문을 해서 int price에 값을 저장해놓고 gerPrice에서 값을 꺼내고 싶었던 것!
    //private int price; //상태를 유지하는 필드 -> 문제 해결 위해 아래 줄과 같이 변경

    public int order(String name, int price){
        System.out.println("name = " + name+ " price = "+price);
        //this.price=price; //여기가 문제!
        return price; //private price 안거치고 바로 return
    }

    //public int getPrice(){
        //return price;
    //}
}
