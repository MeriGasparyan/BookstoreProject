package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.BookAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
}
