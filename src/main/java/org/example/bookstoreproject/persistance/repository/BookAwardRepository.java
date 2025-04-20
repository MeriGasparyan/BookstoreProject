package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Award;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
boolean existsByBookAndAward(Book book, Award award);
List<BookAward> findByBook(Book book);

    Optional<BookAward> findByBookAndAward(Book book, Award award);
}
