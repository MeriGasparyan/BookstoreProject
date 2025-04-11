package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    Optional<Series> findByTitle(String name);
}
