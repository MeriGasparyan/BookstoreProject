package org.example.bookstoreproject.service.services;


import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.dto.*;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("Cart not found"));
        return CartDTO.fromEntity(cart);
    }

    @Transactional
    public CartDTO addItemToCart(Long userId, Long bookId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.getReferenceById(userId));
                    return cartRepository.save(newCart);
                });

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setBook(book);
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        cart.addItem(item);
        return CartDTO.fromEntity(cart);
    }


    @Transactional
    public CartDTO updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found"));
        if (!cart.getItems().contains(item)) {
            throw new NoSuchElementException("Cart item not found in your cart");
        }
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return CartDTO.fromEntity(item.getCart());
    }

    @Transactional
    public void removeItemFromCart(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found"));
        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        cart.removeItem(item);
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("Cart not found"));
        cart.getItems().clear();
    }

}
