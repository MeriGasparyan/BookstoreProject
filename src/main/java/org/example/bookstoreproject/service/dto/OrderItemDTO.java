package org.example.bookstoreproject.service.dto;

import lombok.*;
import org.example.bookstoreproject.persistance.entity.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long bookId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountApplied;

    public static OrderItemDTO fromEntity(OrderItem order) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(order.getId());
        dto.setBookId(order.getBook().getId());
        dto.setQuantity(order.getQuantity());
        dto.setPrice(order.getPrice());
        dto.setDiscountApplied(order.getDiscountApplied());
        return dto;
    }

    public static List<OrderItemDTO> fromEntities(List<OrderItem> orderItems) {
        List<OrderItemDTO> dtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            dtos.add(fromEntity(orderItem));
        }
        return dtos;
    }
}
