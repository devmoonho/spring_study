package jpabook.jpashop.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderFlatDto;
import jpabook.jpashop.repository.query.OrderItemQueryRepositoryDto;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import jpabook.jpashop.repository.query.OrderQueryRepositoryDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OrderApiController
 * 
 * Entity를 노출하지 말 것 Dto로 결과 제공
 * 
 */

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> searchOrderV1() {
        List<Order> result = orderRepository.findAll(new OrderSearch());
        for (Order order : result) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().map(oi -> oi.getItem().getName());
        }
        return result;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> searchOrderV2() {
        List<Order> findOrders = orderRepository.findAll(new OrderSearch());
        List<OrderDto> result = findOrders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }

    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream().map(oi -> new OrderItemDto(oi))
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto {
        private String name;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem oi) {
            this.name = oi.getItem().getName();
            this.orderPrice = oi.getOrderPrice();
            this.count = oi.getCount();
        }
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> searchOrderV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> searchOrderV3_1(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithItemOptimize(offset, limit);
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryRepositoryDto> searchOrderV4() {
        return orderQueryRepository.findAllDto();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryRepositoryDto> searchOrderV5() {
        return orderQueryRepository.findAllDto_Optimize();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryRepositoryDto> searchOrderV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllDto_flat();
        return flats.stream()
                .collect(groupingBy(
                        o -> new OrderQueryRepositoryDto(o.getOrderId(), o.getName(),
                                o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryRepositoryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())))
                .entrySet().stream()
                .map(e -> new OrderQueryRepositoryDto(e.getKey().getOrderId(), e.getKey().getName(),
                        e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }

}
