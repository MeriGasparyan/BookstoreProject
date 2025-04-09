package org.example.bookstoreproject.service.format;

public class PriceFormatter {
    public static Float getPrice(String priceString) {
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
