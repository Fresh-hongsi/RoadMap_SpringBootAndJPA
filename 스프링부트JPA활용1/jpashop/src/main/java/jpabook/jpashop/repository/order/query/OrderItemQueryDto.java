package jpabook.jpashop.repository.order.query;


import lombok.Data;

@Data
public class OrderItemQueryDto {

    private Long orderId;
    private String itemName; //item엔티티에 들어있음
    private int orderPrice;  //orderItem에 들어있음
    private int count; //orderItem에 들어있음

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
