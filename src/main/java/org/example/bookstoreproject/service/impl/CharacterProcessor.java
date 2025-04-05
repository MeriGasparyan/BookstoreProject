package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.CharacterRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.CharacterDTO;
import org.example.bookstoreproject.service.mapper.CharacterMapper;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CharacterProcessor implements CSVColumnProcessor{
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;
    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            String[] charactersArr = ArrayStringParser.getArrElements(row.getCharacters());
            if (charactersArr == null)
                continue;
            for (String character : charactersArr) {
                CharacterDTO characterDTO = new CharacterDTO(character);
                Optional<Character> existing = characterRepository.findByName(characterDTO.getName());
                if (existing.isEmpty()) {
                    Character characterEntity = characterMapper.mapDtoToEntity(characterDTO);
                    characterRepository.save(characterEntity);
                }
            }

        }
    }
}
