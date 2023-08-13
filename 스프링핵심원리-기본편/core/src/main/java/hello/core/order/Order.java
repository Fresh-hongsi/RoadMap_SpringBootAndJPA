package hello.core.order;

public class Order { //주문에서 할인이 다 끝났을 때 만들어지는 객체와 관련된 entity 클래스

    private Long memberId; //회원 아이디
    private String itemName; //삼품 이름 이름
    private int itemPrice; //상품 가격
    private int discountPrice;  //상품 할인 가격

    public Order(Long memberId, String itemName, int itemPrice, int discountPrice) {
        this.memberId = memberId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.discountPrice = discountPrice;
    }

    public int calculatePrice() //비즈니스 로직과 관련된 함수 -> 상품 원가 - 상품 할인금액 을 계산하는 함수
    {
        return itemPrice-discountPrice;

    }

    @Override //출력 형식을 보기 좋게 만들기 위한 toString함수 -> 객체 출력시 이 함수 호출
    public String toString() {
        return "Order{" +
                "memberId=" + memberId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", discountPrice=" + discountPrice +
                '}';
    }

    /////////하단: getter 및 setter
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }
}
