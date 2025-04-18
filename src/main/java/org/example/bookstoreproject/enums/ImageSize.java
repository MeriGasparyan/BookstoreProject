package org.example.bookstoreproject.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ImageSize {
    ORIGINAL("original"),
    SMALL("small"),
    MEDIUM("medium");

    private final String label;

    public static ImageSize fromString(String label) {
        if (label == null || label.trim().isEmpty()) return null;

        String normalized = label.trim().toLowerCase();
        for (ImageSize size : values()) {
            if (size.name().equalsIgnoreCase(label.replace(" ", "_")) ||
                    size.label.toLowerCase().equals(normalized)) {
                return size;
            }
        }
        return null;
    }
}
