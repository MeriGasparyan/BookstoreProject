package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.BookCharacterRepository;
import org.springframework.stereotype.Component;
import org.example.bookstoreproject.service.CSVRow;

import java.util.*;

@Component
@AllArgsConstructor
public class BookCharacterProcessor{

    private final BookCharacterRepository bookCharacterRepository;

    public void process(Map<String, Book> bookMap, Map<String, List<Character>> characterBookMap) {
        List<BookCharacter> existingBookCharacters = bookCharacterRepository.findAll();
        Set<Pair<Long, Long>> existingPairs = new HashSet<>();
        for (BookCharacter bc : existingBookCharacters) {
            existingPairs.add(Pair.of(bc.getBook().getId(), bc.getCharacter().getId()));
        }

        List<BookCharacter> bookCharactersToSave = new ArrayList<>();

        for (Map.Entry<String, List<Character>> entry : characterBookMap.entrySet()) {
            String bookID = entry.getKey().trim();
            Book book = bookMap.get(bookID);

            if (book == null) {
                System.out.println("No book found for bookID: " + bookID);
                continue;
            }

            for (Character character : entry.getValue()) {
                Pair<Long, Long> pair = Pair.of(book.getId(), character.getId());
                if (!existingPairs.contains(pair)) {
                    BookCharacter bookCharacter = new BookCharacter(book, character);
                    bookCharactersToSave.add(bookCharacter);
                    existingPairs.add(pair);
                }
            }
        }

        if (!bookCharactersToSave.isEmpty()) {
            bookCharacterRepository.saveAll(bookCharactersToSave);
        }
    }
}
