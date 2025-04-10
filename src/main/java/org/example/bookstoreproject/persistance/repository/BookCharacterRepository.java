package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookCharacter;
import org.example.bookstoreproject.persistance.entry.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {
boolean existsByBookAndCharacter(Book book, Character character);
List<BookCharacter> findByBook(Book book);
}
