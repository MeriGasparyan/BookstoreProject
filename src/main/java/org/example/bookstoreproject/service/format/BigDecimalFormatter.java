package org.example.bookstoreproject.service.format;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BigDecimalFormatter {
    public BigDecimal getBigDecimal(String priceString) {
        if (priceString == null || priceString.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(priceString);  // Correct method
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
