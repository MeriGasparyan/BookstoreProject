package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookAuthorRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookAuthorProcessor{

    private final BookAuthorRepository bookAuthorRepository;

    @Transactional
    public void process(Map<String, Book> bookMap, Map<String, List<Author>> authorBookMap) {
        List<BookAuthor> bookAuthorList = bookAuthorRepository.findAll();
        List<BookAuthor> newBookAuthorListToSave = new ArrayList<>();
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
