package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.repository.GenreRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class GenreProcessor {

    private final GenreRepository genreRepository;

    @Transactional // Ensure database operations are transactional
    public Pair<Map<String, Genre>, Map<String, List<Genre>>> process(List<CSVRow> data) {
        Map<String, List<Genre>> genreBookMap = new ConcurrentHashMap<>();
        Map<String, Genre> existingGenreMap = new ConcurrentHashMap<>();
        List<Genre> newGenresToSave = new CopyOnWriteArrayList<>();

        // Fetch existing genres once
        List<Genre> genreList = genreRepository.findAll();
        genreList.forEach(genre -> existingGenreMap.put(genre.getName(), genre));

        data.parallelStream().forEach(row -> {
            String[] genresArr = ArrayStringParser.getArrElements(row.getGenres());
            if (genresArr == null) return;

            List<Genre> genresForBook = new CopyOnWriteArrayList<>(); // Thread-safe list

            for (String genreName : genresArr) {
                String trimmedGenreName = genreName.trim();
                // Atomic operation to get or create genre
                Genre genre = existingGenreMap.computeIfAbsent(trimmedGenreName, k -> {
                    Genre newGenre = new Genre(trimmedGenreName);
                    newGenresToSave.add(newGenre); // Add to thread-safe list
                    return newGenre;
                });
                genresForBook.add(genre);
            }
            genreBookMap.compute(row.getBookID().trim(), (bookId, genreListForBook) -> {
                if (genreListForBook == null) {
                    genreListForBook = new CopyOnWriteArrayList<>();
                }
                genreListForBook.addAll(genresForBook); // Add all genres for this book
                return genreListForBook;
            });

        });

        // Save new genres outside the stream
        if (!newGenresToSave.isEmpty()) {
            genreRepository.saveAll(newGenresToSave);
        }

        return Pair.of(existingGenreMap, genreBookMap);
    }
}