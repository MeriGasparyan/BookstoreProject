package org.example.bookstoreproject.service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BookPriceUpdateDTO {
    @DecimalMin("0.01")
    private BigDecimal price;

    @Min(0)
    @Max(100)
    private BigDecimal discount = BigDecimal.ZERO;

}