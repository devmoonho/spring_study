package jpabook.jpashop.repository.query;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

/**
 * OrderRepositoryQuery
 */

@Repository
@RequiredArgsConstructor
public class OrderRepositoryQuery {

    private final EntityManager em;

    public List<OrderRepositoryQueryDto> findAllOrderDto() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderRepositoryQueryDto("
                        + "o.id, m.name, o.orderDate, o.status, d.address)"
                        + "from Order o join o.member m join o.delivery d ",
                OrderRepositoryQueryDto.class).getResultList();
    }
}
