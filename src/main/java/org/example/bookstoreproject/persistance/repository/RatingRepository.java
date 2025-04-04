package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.bookstoreproject.persistance.entry.Rating;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
