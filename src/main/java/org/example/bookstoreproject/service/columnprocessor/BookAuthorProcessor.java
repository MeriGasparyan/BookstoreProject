package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
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
    private final AuthorRepository authorRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Author>> authorBookMap = authorProcessor.getAuthorBookMap();
        List<Book> bookList = bookRepository.findAll();
        List<BookAuthor> bookAuthorList = bookAuthorRepository.findAll();
        List<BookAuthor> newBookAuthorListToSave = new ArrayList<>();
        Map<String, Book> bookMap = new HashMap<>();
        for (Book book : bookList) {
            bookMap.put(book.getBookID(), book);
        }

        Set<Pair<Long, Long>> existingAuthorBookSet = new HashSet<>();
        for (BookAuthor bookAuthor : bookAuthorList) {
            existingAuthorBookSet.add(Pair.of(bookAuthor.getAuthor().getId(), bookAuthor.getBook().getId()));
        }

        for (Map.Entry<String, List<Author>> entry : authorBookMap.entrySet()) {
            List<Author> authors = entry.getValue();
            String bookID = entry.getKey().trim();
            Book book = bookMap.get(bookID);
            if (book != null) {
                for (Author author : authors) {
                    Pair<Long, Long> pair = Pair.of(book.getId(),author.getId());
                    if (!existingAuthorBookSet.contains(pair)) {
                        BookAuthor bookAuthor = new BookAuthor(book, author);
                        newBookAuthorListToSave.add(bookAuthor);
                        existingAuthorBookSet.add(pair);
                    }
                }
            }
        }

        if (!newBookAuthorListToSave.isEmpty()) {
            bookAuthorRepository.saveAll(newBookAuthorListToSave);
        }
    }
}
