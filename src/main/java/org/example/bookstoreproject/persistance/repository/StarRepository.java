package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface StarRepository extends JpaRepository<Star, Long> {
    Optional<Star> findByLevel(RatingStarNumber level);

    @Query("select s from Star s")
    Set<Star> findAllStars();
}
