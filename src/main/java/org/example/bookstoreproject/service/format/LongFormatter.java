package org.example.bookstoreproject.service.format;

import org.springframework.stereotype.Component;

@Component
public class LongFormatter {
    public Long getLong(String stringValue) {
        if (stringValue == null || stringValue.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(stringValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
