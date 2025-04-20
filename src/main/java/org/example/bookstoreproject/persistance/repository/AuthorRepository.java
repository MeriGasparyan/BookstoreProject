package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    List<Author> findByNameIn(List<String> roleNames);

    @Query("SELECT a.name FROM Author a")
    Set<String> findAllBookNames();

    @Query("""
            SELECT DISTINCT a FROM Author a
            LEFT JOIN BookAuthor ba ON ba.author.id = a.id
            LEFT JOIN Book b ON ba.book.id = b.id
            WHERE (:id IS NULL OR a.id = :id)
            AND (:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    List<Author> searchAuthor(@Param("id") Long id,
                              @Param("name") String name,
                              Pageable pageable);

}
