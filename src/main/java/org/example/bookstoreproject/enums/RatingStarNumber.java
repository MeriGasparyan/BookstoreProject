package org.example.bookstoreproject.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum RatingStarNumber {
    ONE_STAR(1, "One Star"),
    TWO_STAR(2, "Two Stars"),
    THREE_STAR(3, "Three Stars"),
    FOUR_STAR(4, "Four Stars"),
    FIVE_STAR(5, "Five Stars");

    private final int value;
    private final String label;

    public static RatingStarNumber fromInt(int value) {
        for (RatingStarNumber rating : values()) {
            if (rating.value == value) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating star: " + value);
    }

    public static RatingStarNumber fromString(String label) {
        if (label == null || label.trim().isEmpty()) return null;

        String normalized = label.trim().toLowerCase();
        for (RatingStarNumber rating : values()) {
            if (rating.name().equalsIgnoreCase(label.replace(" ", "_")) ||
                    rating.label.toLowerCase().equals(normalized)) {
                return rating;
            }
        }
        return null;
    }
}
