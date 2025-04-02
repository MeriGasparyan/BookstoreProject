package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {
}
