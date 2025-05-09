package org.example.bookstoreproject.service.dto;
import lombok.*;
import org.example.bookstoreproject.enums.PaymentMethod;
import org.example.bookstoreproject.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private String orderNumber;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime paymentDate;
}