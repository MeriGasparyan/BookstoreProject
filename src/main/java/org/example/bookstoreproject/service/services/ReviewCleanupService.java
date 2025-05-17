package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;

import org.example.bookstoreproject.enums.ReviewStatus;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.example.bookstoreproject.persistance.repository.UserBookRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCleanupService {
    private final UserBookRatingRepository ratingRepository;

    @Value("${cleanup.rejected.after.days:30}")
    private int daysToKeepRejected;

    @Transactional
    public void cleanupRejectedReviews() {
        Instant cutoffDate = Instant.now().minus(daysToKeepRejected, ChronoUnit.DAYS);
        List<UserBookRating> rejectedRatings = ratingRepository.findByReviewStatus(ReviewStatus.REJECTED);
        for (UserBookRating rating : rejectedRatings) {
            rating.setReview(null);
        }
        ratingRepository.saveAll(rejectedRatings);

        ratingRepository.deleteByReviewStatusAndUpdatedAtBefore(
                ReviewStatus.REJECTED, cutoffDate);
    }
}