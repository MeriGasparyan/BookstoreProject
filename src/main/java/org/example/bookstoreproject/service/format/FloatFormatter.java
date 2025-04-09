package org.example.bookstoreproject.service.format;

import org.springframework.stereotype.Component;

@Component
public class FloatFormatter {
    public Float getFloat(String priceString) {
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
