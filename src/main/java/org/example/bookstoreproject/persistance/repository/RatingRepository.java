package org.example.bookstoreproject.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.bookstoreproject.persistance.entry.Rating;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
