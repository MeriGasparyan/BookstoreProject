package org.example.bookstoreproject.service.columnprocessor;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.CharacterRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CharacterProcessor{
    private final CharacterRepository characterRepository;

    public Pair<Map<String, Character>, Map<String, List<Character>>> process(List<CSVRow> data) {
        Map<String, List<Character>> characterBookMap = new HashMap<>();
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

            characterBookMap.put(row.getBookID().trim(), charactersForBook);
        }
        if (!newCharactersToSave.isEmpty()) {
            characterRepository.saveAll(newCharactersToSave);
            newCharactersToSave.clear();
        }
        return Pair.of(existingCharacterMap, characterBookMap);
    }
}
