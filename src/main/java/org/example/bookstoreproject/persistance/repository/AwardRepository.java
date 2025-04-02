package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
}
