package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.FormatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface FormatRepository extends JpaRepository<FormatEntity, Long> {
    Optional<FormatEntity> findByFormat(String formatName);
}
