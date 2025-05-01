package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookGenre;
import org.example.bookstoreproject.persistance.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {
    boolean existsByBookAndGenre(Book book, Genre genre);
    List<BookGenre> findByGenre(Genre genre);
    List<BookGenre> findByBook(Book book);

    Optional<BookGenre> findByBookAndGenre(Book book, Genre genre);
}
