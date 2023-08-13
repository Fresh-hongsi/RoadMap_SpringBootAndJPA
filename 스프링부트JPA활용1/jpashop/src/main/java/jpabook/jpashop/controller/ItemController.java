package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService; //컨트롤러는 서비스를 갖다 쓰는게 사실상 국룰임

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form",new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form)
    {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book); //상품 등록을 service에 맡김
        return "redirect:/"; //저장된 상품 목록 창으로 리다이렉트
    }

    @GetMapping("items") //상품 목록 조회
    public String list(Model model)
    {
        List<Item> items = itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";
    }

    //상품 수정
    @GetMapping("items/{itemId}/edit") //{itemId}: pathVariable
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model)
    {
        Book item = (Book) itemService.findOne(itemId);//itemService에서 해당 id에 맞는 아이템을 가져옴(type casting 진행)

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn((item.getIsbn()));

        model.addAttribute("form",form);
        return "items/updateItemForm";

    }

    //
    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) //여기서 @ModelAttribute의 form은 updateItemForm의 7번쨰 줄의 form임
    {
//        Book book = new Book();
//        book.setId(form.getId()); //id를 갖고 있는 책 객체이므로 준영속 엔티티이다!(데이터베이스에 한번 들어가있는 애의 id로 세팅하고있음)
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());;
//        book.setAuthor((form.getAuthor()));
//        book.setIsbn(form.getIsbn());

//        itemService.saveItem(book); //변경된 사항을 itemService에 넣어주기. save 메서드의 persist가 아닌 merge를 타고 들어가서 실행

        itemService.updateItem(itemId,form.getName(),form.getPrice(),form.getStockQuantity());
        return "redirect:/items";
    }

}
