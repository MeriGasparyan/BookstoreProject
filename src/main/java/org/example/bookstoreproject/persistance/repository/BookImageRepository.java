package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {
    @Query("""
select bi from BookImage bi 
where bi.book.id = :bookId
and bi.imageSize = :imageSize
""")
    Optional<BookImage> findByBookAndImageSize(Long bookId, ImageSize imageSize);
}
