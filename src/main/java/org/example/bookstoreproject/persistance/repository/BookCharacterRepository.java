package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookCharacter;
import org.example.bookstoreproject.persistance.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {
boolean existsByBookAndCharacter(Book book, Character character);
List<BookCharacter> findByBook(Book book);

    Optional<BookCharacter> findByBookAndCharacter(Book book, Character character);
}
