package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {
    Optional<Star> findByLevel(RatingStarNumber level);
}
