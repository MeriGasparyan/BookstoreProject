package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    List<Author> findAll();
}
