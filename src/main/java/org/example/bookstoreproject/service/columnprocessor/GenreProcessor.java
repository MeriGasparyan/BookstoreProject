package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.repository.GenreRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GenreProcessor{
    private final GenreRepository genreRepository;

    public Pair<Map<String, Genre>, Map<String, List<Genre>>> process(List<CSVRow> data) {
        Map<String, List<Genre>> genreBookMap = new HashMap<>();
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
        return Pair.of(existingGenreMap, genreBookMap);
    }
}
