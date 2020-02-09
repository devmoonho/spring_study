package jpabook.jpashop.repository.query;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

/**
 * OrderSimpleQueryRepository
 */

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager em;

    public List<OrderSimpleQueryRepositoryDto> findAllOrderDto() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderSimpleQueryRepositoryDto("
                        + "o.id, m.name, o.orderDate, o.status, d.address)"
                        + "from Order o join o.member m join o.delivery d ",
                OrderSimpleQueryRepositoryDto.class).getResultList();
    }

}
