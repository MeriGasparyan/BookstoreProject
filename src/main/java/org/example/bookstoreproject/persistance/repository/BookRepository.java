package org.example.bookstoreproject.persistance.repository;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findByIsbnAndTitle(String isbn, String title);

    Optional<Book> findByBookIDAndTitle(String bookID, String title);
    Optional<Book> findByTitle(String title);
    Optional<Book> findByBookID(String bookID);
}
