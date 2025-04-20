package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Author;
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
    WHERE (LOWER(CONCAT('%', a.name, '%')) LIKE LOWER(:name) OR :name IS NULL OR a.name IS NULL)
    AND (:id IS NULL OR a.id = :id)
    """)
    List<Author> searchAuthor(@Param("id") Long id,
                               @Param("name") String name);
}
