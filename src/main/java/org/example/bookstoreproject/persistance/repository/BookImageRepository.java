package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {
}
