package org.example.bookstoreproject.service.dto;
import lombok.*;
import org.example.bookstoreproject.enums.PaymentMethod;
import org.example.bookstoreproject.enums.PaymentStatus;
import org.example.bookstoreproject.persistance.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime paymentDate;

    public static PaymentDTO fromEntity(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setMethod(payment.getMethod());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setTransactionId(payment.getTransactionId());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        return paymentDTO;
    }
}