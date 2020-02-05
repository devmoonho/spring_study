package jpabook.jpashop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

/**
 * OrderServiceTest
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("생각에생각", 10000, 8);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품주문시 상태는 ORDER", getOrder.getStatus(), OrderStatus.ORDER);
        assertEquals("주문한 상품 수 ", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량 ", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문하면 재고가 줄어야한다 ", 6, book.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book book =   createBook("lotto", 100, 20);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        
        assertEquals("취소 상태 채크", getOrder.getStatus() , OrderStatus.CANCEL);
        assertEquals("취소 재고 수량 복구.",20, book.getStockQuantity());

    }


    @Test(expected = NotEnoughStockException.class)
    public void 재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("재고수량", 20000, 10);
        int orderCount = 11;

        // when
        orderService.order(member.getId(), item.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외 발생 해야한다");

        // Assertions.assertThat( ).isEqualTo( );
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }


    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "동작구", "1234"));
        em.persist(member);
        return member;
    }
}
