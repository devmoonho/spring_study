package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderRepositoryQuery;
import jpabook.jpashop.repository.query.OrderRepositoryQueryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * xToOne( ManyToOne, OneToOne ) OrderSimpleApiController
 * 
 * Order Order -> Member Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderRepositoryQuery orderRepositoryQuery; 

    @GetMapping("/api/v1/simple-orders")
    public List<Order> searchOrderV1() {
        List<Order> findAll = orderRepository.findAll(new OrderSearch());
        return findAll;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> searchOrderV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDto> result =
                orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> searchOrderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList()); 
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderRepositoryQueryDto> searchOrderV4(){
       return orderRepositoryQuery.findAllOrderDto();
    }

    @Data
    @AllArgsConstructor
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();

        }
    }
}
