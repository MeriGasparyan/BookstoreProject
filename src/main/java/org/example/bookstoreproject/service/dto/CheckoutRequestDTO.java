package org.example.bookstoreproject.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.bookstoreproject.enums.PaymentMethod;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequestDTO {
    @NotBlank
    private String shippingAddress;

    @NotNull
    private PaymentMethod paymentMethod;

    private String promoCode;
}
