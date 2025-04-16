package org.example.bookstoreproject.persistance.repository;
import org.example.bookstoreproject.persistance.entry.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT b.bookID FROM Book b")
    Set<String> findAllBookIds();

    List<Book> findAllByBookIDIn(Set<String> bookIDs);

    @Query("SELECT b.bookID FROM Book b WHERE b.bookID IN :ids")
    Set<String> findBookIdsByBookIdIn(@Param("ids") List<String> ids);
}
