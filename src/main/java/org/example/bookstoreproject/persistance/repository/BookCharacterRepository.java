package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookCharacter;
import org.example.bookstoreproject.persistance.entry.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {
boolean existsByBookAndCharacter(Book book, Character character);

}
