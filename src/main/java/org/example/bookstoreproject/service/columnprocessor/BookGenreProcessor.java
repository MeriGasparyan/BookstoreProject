package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookGenreRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.springframework.stereotype.Component;
import org.example.bookstoreproject.service.CSVRow;

import java.util.*;

@Component
@AllArgsConstructor
public class BookGenreProcessor implements CSVColumnProcessor {

    private final BookGenreRepository bookGenreRepository;
    private final GenreProcessor genreProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Genre>> genreBookMap = genreProcessor.getGenreBookMap();

        List<Book> allBooks = bookRepository.findAll();
        List<BookGenre> existingBookGenres = bookGenreRepository.findAll();

        Map<String, Book> bookMap = new HashMap<>();
        for (Book book : allBooks) {
            bookMap.put(book.getBookID(), book);
        }

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
