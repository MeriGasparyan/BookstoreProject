package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.repository.BookAwardRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookAwardProcessor{

    private final BookAwardRepository bookAwardRepository;

    @Transactional
    public void process(Map<String, Book> bookCache, Map<String, List<Award>> awardBookMap) {
        if (bookCache.isEmpty()) {
            return;
        }
        List<BookAward> existingBookAwards = bookAwardRepository.findAll();
        Set<Pair<Long, Long>> existingPairs = new HashSet<>();
        for (BookAward bookAward : existingBookAwards) {
            existingPairs.add(Pair.of(bookAward.getBook().getId(), bookAward.getAward().getId()));
        }

        List<BookAward> bookAwardsToSave = new ArrayList<>();

        for (Map.Entry<String, List<Award>> entry : awardBookMap.entrySet()) {
            String bookID = entry.getKey().trim();
            Book book = bookCache.get(bookID);
            if (book == null) {
                continue;
            }

            for (Award award : entry.getValue()) {
                Pair<Long, Long> pair = Pair.of(book.getId(), award.getId());
                if (!existingPairs.contains(pair)) {
                    BookAward bookAward = new BookAward(book, award);
                    book.addBookAward(bookAward);
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
