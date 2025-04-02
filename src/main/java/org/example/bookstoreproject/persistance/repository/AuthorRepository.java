package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
