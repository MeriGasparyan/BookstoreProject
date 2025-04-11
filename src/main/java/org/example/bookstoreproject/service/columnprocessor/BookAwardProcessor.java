package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookAwardRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.springframework.stereotype.Component;
import org.example.bookstoreproject.service.CSVRow;

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
        List<Book> allBooks = bookRepository.findAll();
        for (Book book : allBooks) {
            bookCache.put(book.getBookID(), book);
        }

        List<BookAward> existingBookAwards = bookAwardRepository.findAll();
        Set<Pair<Long, Long>> existingPairs = new HashSet<>();
        for (BookAward bookAward : existingBookAwards) {
            existingPairs.add(Pair.of(bookAward.getBook().getId(), bookAward.getAward().getId()));
        }

        Map<String, List<Award>> awardBookMap = awardProcessor.getAwardBookMap();
        List<BookAward> bookAwardsToSave = new ArrayList<>();

        for (Map.Entry<String, List<Award>> entry : awardBookMap.entrySet()) {
            String bookID = entry.getKey().trim();
            Book book = bookCache.get(bookID);
            if (book == null) {
                System.out.println("No book found for bookID: " + bookID);
                continue;
            }

            for (Award award : entry.getValue()) {
                Pair<Long, Long> pair = Pair.of(book.getId(), award.getId());
                if (!existingPairs.contains(pair)) {
                    BookAward bookAward = new BookAward(book, award);
                    bookAwardsToSave.add(bookAward);
                    existingPairs.add(pair);
                }
            }
        }

        if (!bookAwardsToSave.isEmpty()) {
            bookAwardRepository.saveAll(bookAwardsToSave);
        }
    }
}
