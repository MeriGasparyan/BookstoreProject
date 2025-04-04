package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.service.dto.GenreDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreMapper {
    public Genre mapDtoToEntity(GenreDTO dto) {
        return new Genre(dto.getName());
    }
}
