package org.example.bookstoreproject.persistance.repository;
import org.example.bookstoreproject.persistance.entry.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findByIsbnAndTitle(String isbn, String title);
}
