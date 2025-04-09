package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.CharacterRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.CharacterDTO;
import org.example.bookstoreproject.service.mapper.CharacterMapper;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Order(3)
public class CharacterProcessor implements CSVColumnProcessor{
    private final CharacterRepository characterRepository;
    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            String[] charactersArr = ArrayStringParser.getArrElements(row.getCharacters());
            if (charactersArr == null)
                continue;
            for (String character : charactersArr) {
                Optional<Character> existing = characterRepository.findByName(character);
                if (existing.isEmpty()) {
                    Character characterEntity = new Character(character);
                    characterRepository.save(characterEntity);
                }
            }

        }
    }
}
