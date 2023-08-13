package hello.core.order;

public interface OrderService // 상품 주문과 관련된 인터페이스
{

    Order createOrder(Long memberId, String itemName, int itemPrice); //주문 생성
}
