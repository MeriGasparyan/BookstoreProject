package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
    Optional<LanguageEntity> findByLanguage(String languageName);
    List<LanguageEntity> findAll();
}
