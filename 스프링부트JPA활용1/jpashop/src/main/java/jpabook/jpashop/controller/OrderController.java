package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model)
    {
        List<Member> members = memberService.findMembers(); //모든 회원 가져오기
        List<Item> items = itemService.findItems();//모든 상품 가져오기

        model.addAttribute("members",members);
        model.addAttribute("items",items);
        //form형태로 클래스 만들어서 한번에 안넘기고 model에 필요한 것만 만들어서 바로 넘김
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId, @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){
        //@ModelAttribute인 form을 받아온게 아니라 필요한 것들만 requestparam으로 받아옴(memberId란 name에 담긴 value인 member.id, itemId란 name에 담긴 item.id,count란 name에 담긴 value->여기서 count의 value는 은닉되어있음)

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) //orderSearch 폼클래스를 orderList.html에서 사용하기 위함. 이렇게 Model과 별개로 OrderSearch를 modelattribute로 해놓으면 model에 자동으로 같이 담겨서 넘어간다
    {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);
        //model.addAttribute("orderSearch",orderSearch); 이 코드가 생략되어있다고 보면 됨

        return "order/orderList";
    }

    //주문 취소
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId)
    {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
