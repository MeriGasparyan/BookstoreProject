package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;

import org.example.bookstoreproject.enums.ReviewStatus;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.example.bookstoreproject.persistance.repository.UserBookRatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCleanupService {
    private final UserBookRatingRepository ratingRepository;

    @Transactional
    public void cleanupRejectedReviews() {
        List<UserBookRating> rejectedRatings = ratingRepository.findByReviewStatus(ReviewStatus.REJECTED);
        for (UserBookRating rating : rejectedRatings) {
            ratingRepository.deleteById(rating.getId());
        }
    }
}