package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.BookAwardRepository;
import org.example.bookstoreproject.persistance.repository.BookCharacterRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BookCharacterProcessor implements CSVColumnProcessor{
    private final BookCharacterRepository bookCharacterRepository;
    private final CharacterProcessor characterProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Character>> characterBookMap =
                characterProcessor.getCharacterBookMap();
        for(Map.Entry<String, List<Character>> entry : characterBookMap.entrySet()){
            List<Character> characters = entry.getValue();
            Optional<Book> book =
                    bookRepository.findByBookID(entry.getKey().trim());
            if(book.isPresent()){
                for(Character character : characters){
                    if(bookCharacterRepository.existsByBookAndCharacter(book.get(), character)){
                        continue;
                    }
                    BookCharacter bookCharacter = new BookCharacter(book.get(), character);
                    bookCharacterRepository.save(bookCharacter);
                }

            }
        }

    }
}
