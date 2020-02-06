package jpabook.jpashop.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

/**
 * ItemService
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId(); 
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
         return itemRepository.findOne(itemId);
    }

    @Transactional
    public void updateItem(Long itemId, Book param) {
        // 1차캐시에 저장됨 -> 영속성 상태 -> flush, commit 없이 DB 반영됨 (쿼리동작) 
        Item findItem = itemRepository.findOne(param.getId());
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
    }

    @Transactional
    public void updateItem(Long itemId, ItemServiceDto itemServiceDto) {

    }
}
