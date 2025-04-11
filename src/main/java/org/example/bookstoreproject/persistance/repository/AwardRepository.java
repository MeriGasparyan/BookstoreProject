package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    Optional<Award> findByTitle(String name);
    List<Award> findAll();

}
