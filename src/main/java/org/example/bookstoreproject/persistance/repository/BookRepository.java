package org.example.bookstoreproject.persistance.repository;
import org.example.bookstoreproject.persistance.entry.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
