package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OffensiveReviewDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private String review;
    private String sentiment;
    private boolean containsOffensiveWords;
}
