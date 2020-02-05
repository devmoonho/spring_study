package jpabook.jpashop.service;

import static org.junit.Assert.assertEquals;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.repository.ItemRepository;

/**
 * ItemServiceTest
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
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
