package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.CharacterRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class CharacterProcessor {

    private final CharacterRepository characterRepository;

    @Transactional
    public Map<String, List<Character>> process(List<CSVRow> data) {
        Map<String, List<Character>> characterBookMap = new ConcurrentHashMap<>();
        Map<String, Character> existingCharacterMap = new ConcurrentHashMap<>();
        List<Character> newCharactersToSave = new CopyOnWriteArrayList<>();

        List<Character> characterList = characterRepository.findAll();
        characterList.forEach(character -> existingCharacterMap.put(character.getName(), character));

        data.parallelStream().forEach(row -> {
            String[] charactersArr = ArrayStringParser.getArrElements(row.getCharacters());
            if (charactersArr == null) return;

            List<Character> charactersForBook = new CopyOnWriteArrayList<>();
            for (String characterName : charactersArr) {
                String trimmedCharacterName = characterName.trim();
                Character character = existingCharacterMap.computeIfAbsent(trimmedCharacterName, k -> {
                    Character newCharacter = new Character(trimmedCharacterName);
                    newCharactersToSave.add(newCharacter);
                    return newCharacter;
                });
                charactersForBook.add(character);
            }

            characterBookMap.compute(row.getBookID().trim(), (bookId, characterListForBook) -> {
                if (characterListForBook == null) {
                    characterListForBook = new CopyOnWriteArrayList<>();
                }
                characterListForBook.addAll(charactersForBook);
                return characterListForBook;
            });
        });

        if (!newCharactersToSave.isEmpty()) {
            characterRepository.saveAll(newCharactersToSave);
        }
        return characterBookMap;
    }
}