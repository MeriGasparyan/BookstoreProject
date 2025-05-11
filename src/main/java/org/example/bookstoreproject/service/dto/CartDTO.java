package org.example.bookstoreproject.service.dto;

import lombok.*;
import org.example.bookstoreproject.persistance.entity.Cart;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CartDTO fromEntity(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.id = cart.getId();
        cartDTO.userId = cart.getUser().getId();
        cartDTO.createdAt = cart.getCreatedAt();
        cartDTO.updatedAt = cart.getUpdatedAt();
        cartDTO.items = CartItemDTO.fromEntities(cart.getItems());
        return cartDTO;
    }
}
