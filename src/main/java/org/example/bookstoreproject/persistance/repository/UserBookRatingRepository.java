package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface UserBookRatingRepository extends JpaRepository<UserBookRating, Long> {
    Optional<UserBookRating> findByUserIdAndBookId(Long userId, Long bookId);

    void deleteByUserIdAndBookId(Long userId, Long bookId);

    Page<UserBookRating> findByBookIdAndReviewIsNotNull(Long bookId, Pageable pageable);

    Optional<UserBookRating> findByIdAndBookId(Long reviewId, Long bookId);
}
