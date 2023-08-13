package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //상품 등록
    @Transactional
    public void saveItem(Item item)
    {
        itemRepository.save(item);
    }

    /**
     * 영속성 컨텍스트가 자동 변경
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity)
    {
        Item findItem = itemRepository.findOne(itemId);//레포지토리에서 해당 id의 책을 찾음 , 트랜잭션 내에서 find하므로 '영속'(jpa가 관리하는)상태의 book을 찾을 수잇음
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        //itemRepository.save(findItem); //얘를 호출할 필요가 없음 -> 이미 jpa가 관리하는 애이므로 트랜잭션 커밋 시점에 알아서 변경감지가 돼서 db에 반영됨
        //return findItem; //세팅 다 된 애를 반환해줌
    }

    //모든 상품 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    //상품 하나 조회
    public Item findOne(Long id)
    {
        return itemRepository.findOne(id);
    }


}
