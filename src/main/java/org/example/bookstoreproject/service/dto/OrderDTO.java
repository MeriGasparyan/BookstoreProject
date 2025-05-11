package org.example.bookstoreproject.service.dto;

import lombok.*;
import org.example.bookstoreproject.enums.OrderStatus;
import org.example.bookstoreproject.persistance.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private List<OrderItemDTO> items;
    private BigDecimal total;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PaymentDTO payment;
    private String shippingAddress;

    public static OrderDTO fromEntity(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setUserEmail(order.getUser().getEmail());
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setPayment(PaymentDTO.fromEntity(order.getPayment()));
        dto.setShippingAddress(order.getShippingAddress());
        dto.setItems(OrderItemDTO.fromEntities(order.getItems()));
        return dto;
    }

    public static List<OrderDTO> fromEntities(List<Order> orders) {
        List<OrderDTO> dtos = new ArrayList<>();
        for (Order order : orders) {
            dtos.add(fromEntity(order));
        }
        return dtos;
    }
}
