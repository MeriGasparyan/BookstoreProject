package org.example.bookstoreproject.service.format;

import org.springframework.stereotype.Component;

@Component
public class PriceFormatter {
    public Float getPrice(String priceString) {
        if (priceString == null || priceString.isEmpty()) {
            return null;
        }
        try {
            return Float.parseFloat(priceString);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
