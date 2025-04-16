package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookGenreRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookGenreProcessor{

    private final BookGenreRepository bookGenreRepository;

    @Transactional
    public void process(Map<String, Book> bookMap, Map<String, List<Genre>> genreBookMap) {

        List<BookGenre> existingBookGenres = bookGenreRepository.findAll();
        Set<Pair<Long, Long>> existingPairs = new HashSet<>();
        for (BookGenre bg : existingBookGenres) {
            existingPairs.add(Pair.of(bg.getBook().getId(), bg.getGenre().getId()));
        }

        List<BookGenre> bookGenresToSave = new ArrayList<>();

        for (Map.Entry<String, List<Genre>> entry : genreBookMap.entrySet()) {
            String bookID = entry.getKey().trim();
            Book book = bookMap.get(bookID);

            if (book == null) {
                System.out.println("No book found for bookID: " + bookID);
                continue;
            }

            for (Genre genre : entry.getValue()) {
                Pair<Long, Long> pair = Pair.of(book.getId(), genre.getId());
                if (!existingPairs.contains(pair)) {
                    BookGenre bookGenre = new BookGenre(book, genre);
                    bookGenresToSave.add(bookGenre);
                    existingPairs.add(pair);
                }
            }
        }

        if (!bookGenresToSave.isEmpty()) {
            bookGenreRepository.saveAll(bookGenresToSave);
        }
    }
}
