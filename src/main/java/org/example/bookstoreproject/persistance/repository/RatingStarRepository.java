package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.RatingStar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingStarRepository extends JpaRepository<RatingStar, Long> {
}
