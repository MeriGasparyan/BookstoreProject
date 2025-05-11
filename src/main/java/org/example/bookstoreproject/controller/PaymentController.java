package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.PaymentMethod;
import org.example.bookstoreproject.service.dto.PaymentDTO;
import org.example.bookstoreproject.service.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}/process")
    public ResponseEntity<PaymentDTO> processPayment(
            @PathVariable Long orderId,
            @RequestParam PaymentMethod method) {
        return ResponseEntity.ok(paymentService.processPayment(orderId, method));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentDTO> refund(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.refundPayment(paymentId));
    }
}
