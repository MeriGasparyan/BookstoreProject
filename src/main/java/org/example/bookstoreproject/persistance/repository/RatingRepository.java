package org.example.bookstoreproject.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.bookstoreproject.persistance.entry.Rating;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByRatingAndBook_Id(Float rating, Long bookId);
    Optional<Rating> findByRatingAndBook_Id(Float rating, Long bookId);
    Optional<Rating> findByRating(Rating rating);
    Optional<Rating> findByBook_Id(Long bookId);
}
