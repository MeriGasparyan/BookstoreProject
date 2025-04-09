package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bookstoreproject.persistance.entry.Award;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.repository.GenreRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(4)
public class GenreProcessor implements CSVColumnProcessor {
    private final GenreRepository genreRepository;

    @Getter
    private final Map<String, List<Genre>> genreBookMap;

    public GenreProcessor(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
        this.genreBookMap = new HashMap<>();
    }

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            List<Genre> genres = new ArrayList<>();
            String[] genresArr = ArrayStringParser.getArrElements(row.getGenres());
            if (genresArr == null)
                continue;
            for (String genre : genresArr) {
                Optional<Genre> existing = genreRepository.findByName(genre);
                if (existing.isEmpty()) {
                    Genre genreEntity = new Genre(genre);
                    genres.add(genreEntity);
                    genreRepository.save(genreEntity);
                }
            }
            genreBookMap.put(row.getBookID().trim(), genres);
        }
    }
}
