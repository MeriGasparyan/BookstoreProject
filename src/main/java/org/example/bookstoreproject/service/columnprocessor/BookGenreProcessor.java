package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookGenre;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.repository.BookGenreRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BookGenreProcessor implements CSVColumnProcessor {
    private final BookGenreRepository bookGenreRepository;
    private final GenreProcessor genreProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Genre>> genreBookMap =
                genreProcessor.getGenreBookMap();
        for(Map.Entry<String, List<Genre>> entry : genreBookMap.entrySet()){
            List<Genre> genres = entry.getValue();
            Optional<Book> book =
                    bookRepository.findByBookID(entry.getKey().trim());
            if(book.isPresent()){
                for(Genre genre : genres){
                    if(bookGenreRepository.existsByBookAndGenre(book.get(), genre)){
                        continue;
                    }
                    BookGenre bookGenre = new BookGenre(book.get(), genre);
                    bookGenreRepository.save(bookGenre);
                }

            }
        }

    }
}


