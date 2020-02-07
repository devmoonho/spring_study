package jpabook.jpashop.service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.stereotype.Component;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;

/**
 * InitDb
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
       
        private final EntityManager em;

        public void dbInit1(){
            Member member1 = createMember("userA", new Address("seoul", "haewondae", "12345"));

            Book book1 = createBook("JPA1 BOOK", 100, 10000);
            Book book2 = createBook("JPA2 BOOK", 100, 20000);

            OrderItem orderItems1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItems2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member1.getAddress());

            Order order = Order.createOrder(member1, delivery, orderItems1, orderItems2);
            em.persist(order);
        }

        public void dbInit2(){
            Member member2 = createMember("userB", new Address("busan", "donjack", "001001"));

            Book book1 = createBook("Spring1 BOOK", 50, 40000);
            Book book2 = createBook("Spring2 BOOK", 50, 30000);

            OrderItem orderItems1 = OrderItem.createOrderItem(book1, 40000, 5);
            OrderItem orderItems2 = OrderItem.createOrderItem(book2, 30000, 6);

            Delivery delivery = new Delivery();
            delivery.setAddress(member2.getAddress());

            Order order = Order.createOrder(member2, delivery, orderItems1, orderItems2);
            em.persist(order);
        }

        private Member createMember(String name, Address address) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(address);
            em.persist(member);
            return member;
        }

        private Book createBook(String name, int stockQuantity, int price) {
            Book book = new Book(); 
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            em.persist(book);
            return book;
        }
    }
    
}