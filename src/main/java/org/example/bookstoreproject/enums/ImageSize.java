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
}
