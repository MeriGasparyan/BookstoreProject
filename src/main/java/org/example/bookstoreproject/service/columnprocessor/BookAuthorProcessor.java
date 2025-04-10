package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookAuthor;
import org.example.bookstoreproject.persistance.repository.BookAuthorRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.springframework.stereotype.Component;
import org.example.bookstoreproject.service.CSVRow;

import java.util.*;

@Component
@AllArgsConstructor
public class BookAuthorProcessor implements CSVColumnProcessor {

    private final BookAuthorRepository bookAuthorRepository;
    private final AuthorProcessor authorProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Author>> authorBookMap = authorProcessor.getAuthorBookMap();
        Map<String, Book> bookCache = new HashMap<>();
        Set<BookAuthor> bookAuthorsToSave = new HashSet<>();
        Set<String> bookIDs = authorBookMap.keySet();
        loadBooks(bookIDs, bookCache);

        for (Map.Entry<String, List<Author>> entry : authorBookMap.entrySet()) {
            List<Author> authors = entry.getValue();
            String bookID = entry.getKey().trim();
            Book book = bookCache.get(bookID);
            if (book != null) {
                for (Author author : authors) {
                    if (!bookAuthorRepository.existsByBookAndAuthor(book, author)) {
                        bookAuthorsToSave.add(new BookAuthor(book, author));
                    }
                }
            }
        }

        if (!bookAuthorsToSave.isEmpty()) {
            bookAuthorRepository.saveAll(bookAuthorsToSave);
        }
    }

    private void loadBooks(Set<String> bookIDs, Map<String, Book> bookCache) {
        List<Book> books = bookRepository.findAllByBookIDIn(bookIDs);
        for (Book book : books) {
            bookCache.put(book.getBookID().trim(), book);
        }
    }
}
