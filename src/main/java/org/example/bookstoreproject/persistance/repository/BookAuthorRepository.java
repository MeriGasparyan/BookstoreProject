package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    boolean existsByBookAndAuthor(Book book, Author author);

    List<BookAuthor> findByAuthorId(Long authorId);
}
