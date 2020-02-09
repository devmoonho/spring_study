package jpabook.jpashop.repository.query;

import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OrderQueryRepositoryDto
 */
@Data
@EqualsAndHashCode(of="orderId")
public class OrderQueryRepositoryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryRepositoryDto> orderItems;

    public OrderQueryRepositoryDto(Long orderId, String name, LocalDateTime localDate,
            OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = localDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public OrderQueryRepositoryDto(Long orderId, String name, LocalDateTime localDate,
            OrderStatus orderStatus, Address address,
            List<OrderItemQueryRepositoryDto> orderItems) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = localDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderItems = orderItems;
    }
}
