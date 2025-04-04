package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.repository.GenreRepository;
import org.example.bookstoreproject.service.dto.GenreDTO;
import org.example.bookstoreproject.service.mapper.GenreMapper;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class GenreProcessor implements CSVColumnProcessor {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public void process(List<Map<String, String>> data) {
        for (Map<String, String> row : data) {
            System.out.println(row.get("genres"));
            String[] genresArr = ArrayStringParser.getArrElements(row.get("genres"));
            if (genresArr == null)
                continue;
            for (String genre : genresArr) {
                GenreDTO genreDTO = new GenreDTO(genre);
                Optional<Genre> existing = genreRepository.findByName(genreDTO.getName());
                if (existing.isEmpty()) {
                    Genre genreEntity = genreMapper.mapDtoToEntity(genreDTO);
                    genreRepository.save(genreEntity);
                }
            }

        }
    }
}
