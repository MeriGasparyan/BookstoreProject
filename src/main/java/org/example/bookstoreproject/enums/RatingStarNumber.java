package org.example.bookstoreproject.enums;

public enum RatingStarNumber {
    ONE_STAR,
    TWO_STAR,
    THREE_STAR,
    FOUR_STAR,
    FIVE_STAR;

    public static RatingStarNumber fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        return switch (str.trim().toUpperCase().replace(" ", "_")) {
            case "ONE_STAR" -> ONE_STAR;
            case "TWO_STAR" -> TWO_STAR;
            case "THREE_STAR" -> THREE_STAR;
            case "FOUR_STAR" -> FOUR_STAR;
            case "FIVE_STAR" -> FIVE_STAR;
            default -> null;
        };
    }

    public static RatingStarNumber fromInt(int value) {
        return switch (value) {
            case 1 -> ONE_STAR;
            case 2 -> TWO_STAR;
            case 3 -> THREE_STAR;
            case 4 -> FOUR_STAR;
            case 5 -> FIVE_STAR;
            default -> throw new IllegalArgumentException("Invalid rating star: " + value);
        };
    }
}