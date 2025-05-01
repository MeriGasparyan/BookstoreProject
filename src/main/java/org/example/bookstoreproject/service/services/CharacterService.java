package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookCharacter;
import org.example.bookstoreproject.persistance.entity.Character;
import org.example.bookstoreproject.persistance.repository.BookCharacterRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.CharacterRepository;
import org.example.bookstoreproject.service.dto.CreateCharacterDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final BookRepository bookRepository;
    private final BookCharacterRepository bookCharacterRepository;

    @Transactional
    public Character createCharacter(CreateCharacterDTO characterDTO) {
        String name = characterDTO.getName();
        if (characterRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Character with name '" + name + "' already exists");
        }
        Character character = new Character();
        character.setName(name);
        return characterRepository.save(character);
    }

    @Transactional
    public Book addCharactersToBook(Long bookId, List<Long> characterIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        List<Character> characters = characterRepository.findAllById(characterIds);

        if (characters.size() != characterIds.size()) {
            Set<Long> foundCharacterIds = characters.stream()
                    .map(Character::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = characterIds.stream()
                    .filter(id -> !foundCharacterIds.contains(id))
                    .toList();

            throw new NoSuchElementException("Character(s) with ID(s) " + missingIds + " not found");
        }

        characters.forEach(character -> {
            if (bookCharacterRepository.findByBookAndCharacter(book, character).isEmpty()) {
                book.addBookCharacter(new BookCharacter(book, character));
            }
        });
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeCharactersFromBook(Long bookId, List<Long> characterIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found"));

        List<Character> characters = characterRepository.findAllById(characterIds);
        if (characters.size() != characterIds.size()) {
            Set<Long> foundCharacterIds = characters.stream()
                    .map(Character::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = characterIds.stream()
                    .filter(id -> !foundCharacterIds.contains(id))
                    .toList();

            throw new NoSuchElementException("Character(s) with ID(s) " + missingIds + " not found");
        }

        characters.forEach(character -> {
            bookCharacterRepository.findByBookAndCharacter(book, character)
                    .ifPresentOrElse(
                            bookCharacter -> {
                                book.getBookCharacters().remove(bookCharacter);
                                bookCharacterRepository.delete(bookCharacter);
                                bookCharacter.setBook(null);
                                bookCharacter.setCharacter(null);
                            },
                            () -> {
                                throw new IllegalStateException(
                                        "Character with ID " + character.getId() +
                                                " is not associated with book " + bookId);
                            }
                    );
        });

        return bookRepository.save(book);
    }
}