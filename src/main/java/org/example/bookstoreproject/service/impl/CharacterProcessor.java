package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.CharacterRepository;
import org.example.bookstoreproject.service.dto.CharacterDTO;
import org.example.bookstoreproject.service.mapper.CharacterMapper;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CharacterProcessor implements CSVColumnProcessor, Service{
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;
    @Override
    public void process(List<Map<String, String>> data) {
        for (Map<String, String> row : data) {
            System.out.println(row.get("characters"));
            String[] charactersArr = ArrayStringParser.getArrElements(row.get("characters"));
            if (charactersArr == null)
                continue;
            for (String character : charactersArr) {
                CharacterDTO characterDTO = new CharacterDTO(character);
                System.out.println(characterDTO.getName());
                Character characterEntity = characterMapper.mapDtoToEntity(characterDTO);
                characterRepository.save(characterEntity);
            }

        }
    }

    @Override
    public String value() {
        return "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
