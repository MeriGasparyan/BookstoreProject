package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.CharacterRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(3)
public class CharacterProcessor implements CSVColumnProcessor{
    @Getter
    private final Map<String, List<Character>> characterBookMap;

    private final CharacterRepository characterRepository;

    public CharacterProcessor(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
        this.characterBookMap = new HashMap<>();
    }

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            String[] charactersArr = ArrayStringParser.getArrElements(row.getCharacters());
            if (charactersArr == null)
                continue;
            List<Character> characters = new ArrayList<>();
            for (String character : charactersArr) {
                Optional<Character> existing = characterRepository.findByName(character);
                if (existing.isEmpty()) {
                    Character characterEntity = new Character(character);
                    characters.add(characterEntity);
                    characterRepository.save(characterEntity);
                }
            }
            characterBookMap.put(row.getBookID().trim(), characters);
        }
    }
}
