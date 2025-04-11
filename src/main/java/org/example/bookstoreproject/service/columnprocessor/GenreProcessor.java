package org.example.bookstoreproject.service.columnprocessor;

import lombok.Getter;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.repository.GenreRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(5)
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
        Map<String, Genre> existingGenreMap = new HashMap<>();
        List<Genre> genreList = genreRepository.findAll();
        for (Genre genre : genreList) {
            existingGenreMap.put(genre.getName(), genre);
        }

        List<Genre> newGenresToSave = new ArrayList<>();
        for (CSVRow row : data) {
            String[] genresArr = ArrayStringParser.getArrElements(row.getGenres());
            if (genresArr == null) continue;

            List<Genre> genresForBook = new ArrayList<>();

            for (String genreName : genresArr) {
                Genre genre = existingGenreMap.get(genreName);
                if (genre == null) {
                    genre = new Genre(genreName);
                    existingGenreMap.put(genreName, genre);
                    newGenresToSave.add(genre);
                }
                genresForBook.add(genre);
            }
            genreBookMap.put(row.getBookID().trim(), genresForBook);
        }
        if (!newGenresToSave.isEmpty()) {
            genreRepository.saveAll(newGenresToSave);
            newGenresToSave.clear();
        }
    }
}
