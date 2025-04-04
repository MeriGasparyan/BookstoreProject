package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.business.GenreC;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.service.dto.GenreDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreMapper {
    public GenreDTO mapToDto(GenreC genre) {
        if (genre == null) {
            return null;
        }

        return new GenreDTO(
                genre.getName()
        );
    }

    public List<GenreDTO> mapToDtos(List<GenreC> list) {
        List<GenreDTO> dtos = new ArrayList<>();

        for (GenreC genre : list) {
            dtos.add(mapToDto(genre));
        }

        return dtos;
    }

    public GenreC mapDtoToGenres(GenreDTO dto) {
        return new GenreC(dto.getName());
    }
    public Genre mapDtoToEntity(GenreDTO dto) {
        return new Genre(dto.getName());
    }
}
