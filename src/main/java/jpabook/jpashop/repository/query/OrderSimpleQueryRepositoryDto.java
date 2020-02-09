package jpabook.jpashop.repository.query;

import java.time.LocalDateTime;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * OrderSimpleQueryRepositoryDto
 */

@Data
@AllArgsConstructor
public class OrderSimpleQueryRepositoryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryRepositoryDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress();
    }
}

