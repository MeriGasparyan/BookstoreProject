package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entity.Genre;
import org.example.bookstoreproject.service.dto.GenreDTO;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public Genre mapDtoToEntity(GenreDTO dto) {
        return new Genre(dto.getName());
    }
}
