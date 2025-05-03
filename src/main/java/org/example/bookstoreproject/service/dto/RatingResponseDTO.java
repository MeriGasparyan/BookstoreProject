package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entity.Star;
import org.example.bookstoreproject.persistance.entity.UserBookRating;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponseDTO {
    private Long ratingId;
    private Long userId;
    private Long bookId;
    private RatingStarNumber star;
    private Instant updatedAt;

    public static RatingResponseDTO fromEntity(UserBookRating rating) {
        return new RatingResponseDTO(
                rating.getId(),
                rating.getUser().getId(),
                rating.getBook().getId(),
                rating.getStar().getLevel(),
                rating.getUpdatedAt()
        );
    }
}