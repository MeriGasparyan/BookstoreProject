package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.repository.GenreRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.GenreDTO;
import org.example.bookstoreproject.service.mapper.GenreMapper;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class GenreProcessor implements CSVColumnProcessor {
    private final GenreRepository genreRepository;

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            String[] genresArr = ArrayStringParser.getArrElements(row.getGenres());
            if (genresArr == null)
                continue;
            for (String genre : genresArr) {
                Optional<Genre> existing = genreRepository.findByName(genre);
                if (existing.isEmpty()) {
                    Genre genreEntity = new Genre(genre);
                    genreRepository.save(genreEntity);
                }
            }

        }
    }
}
