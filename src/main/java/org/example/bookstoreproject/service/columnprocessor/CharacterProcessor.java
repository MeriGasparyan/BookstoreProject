package org.example.bookstoreproject.service.columnprocessor;

import lombok.Getter;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.CharacterRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(4)
public class CharacterProcessor implements CSVColumnProcessor {
    @Getter
    private final Map<String, List<Character>> characterBookMap;

    private final CharacterRepository characterRepository;

    public CharacterProcessor(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
        this.characterBookMap = new HashMap<>();
    }

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Character> existingCharacterMap = new HashMap<>();
        List<Character> characterList = characterRepository.findAll();
        for (Character character : characterList) {
            existingCharacterMap.put(character.getName(), character);
        }

        List<Character> newCharactersToSave = new ArrayList<>();
        for (CSVRow row : data) {
            String[] charactersArr = ArrayStringParser.getArrElements(row.getCharacters());
            if (charactersArr == null) continue;

            List<Character> charactersForBook = new ArrayList<>();
            for (String characterName : charactersArr) {
                Character character = existingCharacterMap.get(characterName);
                if (character == null) {
                    character = new Character(characterName);
                    existingCharacterMap.put(characterName, character);
                    newCharactersToSave.add(character);
                }
                charactersForBook.add(character);
            }

            if (!newCharactersToSave.isEmpty()) {
                characterRepository.saveAll(newCharactersToSave);
                newCharactersToSave.clear();
            }

            characterBookMap.put(row.getBookID().trim(), charactersForBook);
        }
    }
}
