package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Rating;
import org.example.bookstoreproject.persistance.entry.RatingStar;
import org.example.bookstoreproject.persistance.entry.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingStarRepository extends JpaRepository<RatingStar, Long> {
    Optional<RatingStar> findByRatingAndStar(Rating rating, Star star);
    List<RatingStar> findByRating(Rating rating);
    List<RatingStar> findAll();
}
