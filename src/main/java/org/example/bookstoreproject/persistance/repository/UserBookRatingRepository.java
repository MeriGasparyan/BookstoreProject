package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.ReviewStatus;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRatingRepository extends JpaRepository<UserBookRating, Long> {
    Optional<UserBookRating> findByUserIdAndBookId(Long userId, Long bookId);

    void deleteByUserIdAndBookId(Long userId, Long bookId);

    Page<UserBookRating> findByBookIdAndReviewIsNotNull(Long bookId, Pageable pageable);

    Optional<UserBookRating> findByIdAndBookId(Long reviewId, Long bookId);

    Page<UserBookRating> findByReviewIsNotNullAndReviewStatus(ReviewStatus status, Pageable pageable);

    Page<UserBookRating> findByReviewStatus(ReviewStatus status, Pageable pageable);

    List<UserBookRating> findByReviewStatus(ReviewStatus status);

    long countByReviewStatus(ReviewStatus status);

    @Query("SELECT r FROM UserBookRating r WHERE r.book.id = :bookId AND r.review IS NOT NULL AND r.reviewStatus IN :statuses")
    Page<UserBookRating> findByBookIdAndReviewIsNotNullAndReviewStatusIn(
            Long bookId,
            List<ReviewStatus> statuses,
            Pageable pageable);

    List<UserBookRating> findByReviewStatusAndUpdatedAtBefore(
            ReviewStatus status,
            Instant cutoffDate);
}
