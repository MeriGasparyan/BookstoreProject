package org.example.bookstoreproject.enums;

import lombok.Getter;


@Getter
public enum PaymentMethod {
    CREDIT_CARD,
    PAYPAL,
    BANK_TRANSFER,
    CRYPTO, APPLE_PAY,
    GOOGLE_PAY,
    CASH_ON_DELIVERY;
    public static PaymentMethod fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }

        String normalized = value.trim().toUpperCase().replace("-", "_").replace(" ", "_");

        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equals(normalized)) {
                return method;
            }
        }

        throw new IllegalArgumentException("Unknown payment method: " + value);
    }

}
