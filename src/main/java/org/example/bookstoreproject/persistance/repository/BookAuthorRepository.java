package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {

}
