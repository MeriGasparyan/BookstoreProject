package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ReviewStatus;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.example.bookstoreproject.persistance.repository.UserBookRatingRepository;
import org.example.bookstoreproject.service.dto.OffensiveReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModerationService {
    private final UserBookRatingRepository ratingRepository;

    public Page<OffensiveReviewDTO> getPendingReviews(int page, int size) {
        Page<UserBookRating> ratings = ratingRepository
                .findByReviewStatus(ReviewStatus.PENDING_REVIEW,
                        PageRequest.of(page, size));

        List<OffensiveReviewDTO> dtos = ratings.stream()
                .map(OffensiveReviewDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, PageRequest.of(page, size),
                ratingRepository.countByReviewStatus(ReviewStatus.PENDING_REVIEW));
    }

    @Transactional
    public void approveReview(Long reviewId) {
        UserBookRating rating = ratingRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        rating.setReviewStatus(ReviewStatus.APPROVED);
        ratingRepository.save(rating);
    }

    @Transactional
    public void rejectReview(Long reviewId) {
        UserBookRating rating = ratingRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        rating.setReviewStatus(ReviewStatus.REJECTED);
        ratingRepository.save(rating);
    }


}