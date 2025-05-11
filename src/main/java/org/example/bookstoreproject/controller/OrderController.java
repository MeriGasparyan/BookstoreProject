package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.OrderStatus;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.CheckoutRequestDTO;
import org.example.bookstoreproject.service.dto.OrderDTO;
import org.example.bookstoreproject.service.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> placeOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CheckoutRequestDTO request) {
        Long userId = userDetails.getId();
        System.out.println(userId);
        return ResponseEntity.ok(orderService.placeOrder(userId, request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.getAllOrders(status));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
