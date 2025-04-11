package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookRatingStar;
import org.example.bookstoreproject.persistance.entry.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingStarRepository extends JpaRepository<BookRatingStar, Long> {
    Optional<BookRatingStar> findByBookAndStar(Book book, Star star);
    List<BookRatingStar> findByBook(Book book);
}
