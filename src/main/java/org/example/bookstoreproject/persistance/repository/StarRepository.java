package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {
    List<Star> findAll();
    Optional<Star> findByLevel(String level);
}
