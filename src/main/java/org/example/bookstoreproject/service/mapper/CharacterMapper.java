package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entity.Character;
import org.example.bookstoreproject.service.dto.CharacterDTO;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {
    public Character mapDtoToEntity(CharacterDTO dto) {
        return new Character(dto.getName());
    }
}
