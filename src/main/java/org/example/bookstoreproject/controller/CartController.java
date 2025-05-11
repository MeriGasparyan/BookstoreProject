package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.CartDTO;
import org.example.bookstoreproject.service.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItemToCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long bookId,
            @RequestParam Integer quantity) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(cartService.addItemToCart(userId, bookId, quantity));
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartDTO> updateItemQuantity(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(cartService.updateCartItem(userId, cartItemId, quantity));
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> removeItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cartItemId) {
        Long userId = userDetails.getId();
        cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
