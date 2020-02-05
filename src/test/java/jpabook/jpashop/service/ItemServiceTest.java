package jpabook.jpashop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.repository.ItemRepository;
import static org.junit.Assert.*;
import javax.persistence.EntityManager;

/**
 * ItemServiceTest
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    EntityManager em;

    @Test
    public void testSave() throws Exception {
        // given
        Album album = new Album();
        album.setArtiest("yeo");

        // when
        Long saveId = itemService.saveItem(album);
        em.flush();

        // then
        assertEquals(album, itemRepository.findOne(saveId)); 
    }
}
