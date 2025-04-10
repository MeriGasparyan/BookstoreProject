package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookAwardRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class BookAwardProcessor implements CSVColumnProcessor {
    private final BookAwardRepository bookAwardRepository;
    private final AwardProcessor awardProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Book> bookCache = new HashMap<>();
        Map<String, List<Award>> awardBookMap = awardProcessor.getAwardBookMap();

        List<Book> allBooks = bookRepository.findAll();
        for (Book book : allBooks) {
            bookCache.put(book.getBookID(), book);
        }
        List<BookAward> bookAwardsToSave = new ArrayList<>();

        for (Map.Entry<String, List<Award>> entry : awardBookMap.entrySet()) {
            String bookID = entry.getKey().trim();
            List<Award> awards = entry.getValue();

            Book book = bookCache.get(bookID);
            if (book != null) {
                for (Award award : awards) {
                    if (bookAwardRepository.existsByBookAndAward(book, award)) {
                        continue;
                    }

                    BookAward bookAward = new BookAward(book, award);
                    bookAwardsToSave.add(bookAward);
                }
            } else {
                System.out.println("No book found for bookID: " + bookID);
            }
        }

        if (!bookAwardsToSave.isEmpty()) {
            bookAwardRepository.saveAll(bookAwardsToSave);
        }
    }
}
