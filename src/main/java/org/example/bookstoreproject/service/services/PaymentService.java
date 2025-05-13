package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.OrderStatus;
import org.example.bookstoreproject.enums.PaymentMethod;
import org.example.bookstoreproject.enums.PaymentStatus;
import org.example.bookstoreproject.exception.ResourceNotFoundException;
import org.example.bookstoreproject.persistance.entity.Order;
import org.example.bookstoreproject.persistance.entity.Payment;
import org.example.bookstoreproject.persistance.repository.OrderRepository;
import org.example.bookstoreproject.persistance.repository.PaymentRepository;
import org.example.bookstoreproject.service.dto.PaymentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentDTO processPayment(Long orderId, PaymentMethod method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order cannot be paid for in its current state");
        }

        Payment payment = new Payment();
        payment.setAmount(order.getTotal());
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);

        try {
            switch (method) {
                case CREDIT_CARD:
                case APPLE_PAY:
                case GOOGLE_PAY:
                    processCardPayment(payment);
                    break;
                case PAYPAL:
                    processPayPalPayment(payment);
                    break;
                case BANK_TRANSFER:
                    processBankTransfer(payment);
                    break;
                case CRYPTO:
                    processCryptoPayment(payment);
                    break;
                case CASH_ON_DELIVERY:
                    payment.setTransactionId("COD_" + UUID.randomUUID());
                    payment.setStatus(PaymentStatus.COMPLETED);
                    break;
            }

            paymentRepository.save(payment);

            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                order.setStatus(OrderStatus.PROCESSING);
                orderRepository.save(order);
            }

            return PaymentDTO.fromEntity(payment);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw e;
        }
    }

    private void processCryptoPayment(Payment payment) {
        System.out.println("Simulating crypto payment");
        payment.setTransactionId("CRYPTO_" + UUID.randomUUID());
        payment.setStatus(PaymentStatus.COMPLETED);
    }

    private void processBankTransfer(Payment payment) {
        System.out.println("Simulating bank transfer");
        payment.setTransactionId("BANK_" + UUID.randomUUID());
        payment.setStatus(PaymentStatus.COMPLETED);
    }

    private void processPayPalPayment(Payment payment) {
        System.out.println("Simulating PayPal payment");
        payment.setTransactionId("PAYPAL_" + UUID.randomUUID());
        payment.setStatus(PaymentStatus.COMPLETED);
    }

    private void processCardPayment(Payment payment) {
        System.out.println("Simulating card payment");
        payment.setTransactionId("CARD_" + UUID.randomUUID());
        payment.setStatus(PaymentStatus.COMPLETED);
    }

    @Transactional
    public PaymentDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return PaymentDTO.fromEntity(payment);
    }

    @Transactional
    public PaymentDTO refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded");
        }

        switch (payment.getMethod()) {
            case CREDIT_CARD:
            case APPLE_PAY:
            case GOOGLE_PAY:
                processCardRefund(payment);
                break;
            case PAYPAL:
                processPayPalRefund(payment);
                break;
            case CRYPTO:
            case BANK_TRANSFER:
                simulateGenericRefund(payment);
                break;
            case CASH_ON_DELIVERY:
                throw new IllegalStateException("Cash on Delivery payments are not refundable");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
        Order order = orderRepository.findByPaymentId(paymentId).orElseThrow();
        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);

        return PaymentDTO.fromEntity(payment);
    }

    private void processPayPalRefund(Payment payment) {
        System.out.println("Simulating PayPal refund for: " + payment.getTransactionId());
    }

    private void processCardRefund(Payment payment) {
        System.out.println("Simulating card refund for: " + payment.getTransactionId());
    }

    private void simulateGenericRefund(Payment payment) {
        System.out.println("Simulating generic refund for: " + payment.getTransactionId());
    }
}
