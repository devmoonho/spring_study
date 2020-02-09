package jpabook.jpashop.repository;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;

/**
 * OrderRepository
 */

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        // 동적쿼리 구현
        // String qlString =
        // "select o from Order o join o.member m where o.status =: status and m.name like :name";

        String qlString = "select o from Order o";

        return em.createQuery(qlString, Order.class)
                // .setParameter("status", orderSearch.getOrderStatus())
                // .setParameter("name", orderSearch.getMemberName()).setMaxResults(1000)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                " select o from Order o " + " join fetch o.member m " + " join fetch o.delivery d ",
                Order.class).getResultList();
    }

    /**
     * 제약사항 OneToMany관계에서 join fetch시 페이징 불가능 OneToMany관계의 Many에서 또다른 하나가 다시 OneToMany가 될 수 없다
     */
    public List<Order> findAllWithItem() {
        return em.createQuery(
                " select distinct o from Order o join fetch o.member m join fetch o.delivery d join fetch o.orderItems oi join fetch oi.item i",
                Order.class).getResultList();
        // 안됨 => Order.class).setFirstResult(1).setMaxResults(100).getResultList();
    }

    /**
     * 제약사항 해결 방법
     * 
     * 1. ManyToOne, OneToOne 관계는 join fetch로 한방쿼리 2. OneToMany 관계는 join fetch 없이 쿼리 발생, 다만
     * default_batch_fetch_size를 통해 사이즈 만큼 in query를 포함한 쿼리 발생 따라서 Many 쪽의 Collection을 여러번 쿼리하지 않고
     * 한번만 쿼리 발생
     * 
     * 페이징 가능
     * 
     * @param limit
     * @param offset
     *
     */
    public List<Order> findAllWithItemOptimize(int offset, int limit) {
        return em
                .createQuery(" select o from Order o join fetch o.member m join fetch o.delivery d",
                        Order.class)
                .setFirstResult(offset).setMaxResults(limit).getResultList();
    }
}
