package org.example.bookstoreproject.persistance.repository;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
