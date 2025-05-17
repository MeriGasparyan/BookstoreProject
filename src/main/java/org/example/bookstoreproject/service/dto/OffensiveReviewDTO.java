package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entity.UserBookRating;

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

    public static OffensiveReviewDTO fromEntity(UserBookRating rating) {
        OffensiveReviewDTO dto = new OffensiveReviewDTO();
        dto.setId(rating.getId());
        dto.setUserId(rating.getUser().getId());
        dto.setBookId(rating.getBook().getId());
        dto.setReview(rating.getReview());
        dto.setSentiment(rating.getSentimentResult());
        dto.setContainsOffensiveWords(rating.getContainsOffensiveWords());
        return dto;
    }
}
