package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.service.dto.CharacterDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CharacterMapper {
    public Character mapDtoToEntity(CharacterDTO dto) {
        return new Character(dto.getName());
    }
}
