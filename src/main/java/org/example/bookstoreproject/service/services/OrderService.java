package org.example.bookstoreproject.service.services;


import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.dto.*;
import org.example.bookstoreproject.enums.OrderStatus;
import org.example.bookstoreproject.enums.PaymentMethod;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderService{

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;


    @Transactional
    public OrderDTO placeOrder(Long userId, CheckoutRequestDTO checkoutRequest) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("Cart not found"));

        Order order = new Order();
        order.setUser(userRepository.findById(userId).orElseThrow());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setShippingAddress(checkoutRequest.getShippingAddress());
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            BigDecimal price = BookDTO.calculateDiscountedPrice(cartItem.getBook().getDiscount(), cartItem.getBook().getPrice());
            orderItem.setPrice(price);
            order.addItem(orderItem);
            BigDecimal unitPrice = orderItem.getPrice() == null ? BigDecimal.ZERO : orderItem.getPrice();
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        order.setTotal(total);

        PaymentMethod paymentMethod = checkoutRequest.getPaymentMethod();

        order = orderRepository.save(order);
        PaymentDTO paymentDTO = paymentService.processPayment(order.getId(), paymentMethod);
        order.setStatus(OrderStatus.PAID);
        order.setPayment(paymentRepository.findById(paymentDTO.getId()).orElseThrow(() -> new NoSuchElementException("Payment not made")));
        cart.getItems().clear();
        return OrderDTO.fromEntity(order);
    }


    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) {
        return OrderDTO.fromEntity(orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found")));
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return OrderDTO.fromEntities(orderRepository.findByUserId(userId));
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders(OrderStatus status) {
        return OrderDTO.fromEntities(orderRepository.findByStatus(status));
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return OrderDTO.fromEntity(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
    }

}
