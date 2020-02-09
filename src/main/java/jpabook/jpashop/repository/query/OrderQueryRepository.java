package jpabook.jpashop.repository.query;

import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

/**
 * OrderQueryRepository
 */

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;


    private List<OrderItemQueryRepositoryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderItemQueryRepositoryDto "+
                " ( oi.order.id, i.name, oi.orderPrice, oi.count ) " +
                " from OrderItem oi " +
                " join oi.item i " +
                " where oi.order.id = :orderId",
                OrderItemQueryRepositoryDto.class).setParameter("orderId", orderId).getResultList();
    }

    private List<OrderQueryRepositoryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderQueryRepositoryDto "+
                " ( o.id, m.name, o.orderDate, o.status, d.address ) " +
                " from Order o join o.member m join o.delivery d",
                OrderQueryRepositoryDto.class).getResultList();
    }

    public List<OrderQueryRepositoryDto> findAllDto() {
        List<OrderQueryRepositoryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryRepositoryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }


    /**
     * findOrderItem 에서 in SQL 로 orderItems을 불러옴
     * 
     * @return
     */
    public List<OrderQueryRepositoryDto> findAllDto_Optimize() {
        List<OrderQueryRepositoryDto> result = findOrders(); // 쿼리 1

        List<OrderItemQueryRepositoryDto> orderItems = findOrderItems(result);
        Map<Object, List<OrderItemQueryRepositoryDto>> orderItemMap = findOrderItemsMap(orderItems);
        // 쿼리 2

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Object, List<OrderItemQueryRepositoryDto>> findOrderItemsMap(
            List<OrderItemQueryRepositoryDto> orderItems) {
        return orderItems.stream().collect(Collectors.groupingBy(
                orderItemQueryRepositoryDto -> orderItemQueryRepositoryDto.getOrderId()));
    }

    private List<OrderItemQueryRepositoryDto> findOrderItems(List<OrderQueryRepositoryDto> result) {
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(toList());

        List<OrderItemQueryRepositoryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderItemQueryRepositoryDto"
                        + "( oi.order.id, i.name, oi.orderPrice, oi.count ) " + "from OrderItem oi "
                        + " join oi.item i " + " where oi.order.id in :orderIds ",
                OrderItemQueryRepositoryDto.class).setParameter("orderIds", orderIds)
                .getResultList();
        return orderItems;
    }

    public List<OrderFlatDto> findAllDto_flat() {
        return em.createQuery("select new jpabook.jpashop.repository.query.OrderFlatDto "
                + " (o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) "
                + " from Order o  " + " join o.member m " + " join o.delivery d "
                + " join o.orderItems oi " + " join oi.item i  ", OrderFlatDto.class)
                .getResultList();
    }
}
