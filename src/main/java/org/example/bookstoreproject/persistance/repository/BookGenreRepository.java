package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookGenre;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {

    boolean existsByBookAndGenre(Book book, Genre genre);
    List<BookGenre> findByGenre(Genre genre);
    List<BookGenre> findByBook(Book book);
    List<BookGenre> findAll();
}
