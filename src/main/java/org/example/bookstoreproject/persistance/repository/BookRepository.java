package org.example.bookstoreproject.persistance.repository;
import org.example.bookstoreproject.persistance.entry.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findByIsbnAndTitle(String isbn, String title);

    Optional<Book> findByBookIDAndTitle(String bookID, String title);
    Optional<Book> findByTitle(String title);
    Optional<Book> findByBookID(String bookID);
    Optional<Book> findById(Long id);


    List<Book> findAll();

    List<Book> findAllByBookIDIn(Set<String> bookIDs);
}
