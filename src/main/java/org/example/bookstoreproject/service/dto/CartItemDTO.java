package org.example.bookstoreproject.service.dto;

import lombok.*;
import org.example.bookstoreproject.persistance.entity.Cart;
import org.example.bookstoreproject.persistance.entity.CartItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long bookId;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime addedAt;

    public static CartItemDTO fromEntity(CartItem cart) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.id = cart.getId();
        cartItemDTO.bookId = cart.getBook().getId();
        cartItemDTO.quantity = cart.getQuantity();
        cartItemDTO.addedAt = cart.getAddedAt();
        return cartItemDTO;
    }

    public static List<CartItemDTO> fromEntities(List<CartItem> cartItems) {
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            cartItemDTOS.add(CartItemDTO.fromEntity(cartItem));
        }
        return cartItemDTOS;
    }
}
