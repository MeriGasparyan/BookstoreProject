package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.RatingStarRepository;
import org.example.bookstoreproject.persistance.repository.StarRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.RatingByStarFormatter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookRatingStarProcessor {

    private final RatingStarRepository ratingStarRepository;
    private final StarRepository starRepository;
    private final RatingByStarFormatter ratingByStarFormatter;

    @Transactional
    public void process(List<CSVRow> data, Map<String, Book> existingBookMap) {
        if (existingBookMap.isEmpty()) {
            return;
        }
        Map<String, Star> starMap = new HashMap<>();
        Set<Pair<Long, String>> existingRatingStarPairs = new HashSet<>();
        List<BookRatingStar> bookRatingStarsToSave = new ArrayList<>();
        List<BookRatingStar> existingBookRatingStars = ratingStarRepository.findAll();

        for (BookRatingStar rs : existingBookRatingStars) {
            existingRatingStarPairs.add(Pair.of(rs.getBook().getId(), rs.getStar().getLevel()));
        }

        for (CSVRow row : data) {
            try {
                Map<RatingStarNumber, Long> ratingsByStar = ratingByStarFormatter.formatRatingsByStar(row.getRatingsByStar());
                if (ratingsByStar == null) {
                    continue;
                }

                for (Map.Entry<RatingStarNumber, Long> entry : ratingsByStar.entrySet()) {
                    Star star = starMap.computeIfAbsent(entry.getKey().name(), starLevel ->
                            starRepository.findByLevel(starLevel).orElse(null));

                    if (star == null) {
                        System.out.println("No star found for level: " + entry.getKey().name());
                        continue;
                    }

                    Book book = existingBookMap.get(row.getBookID().trim());
                    if(book == null) {
                        continue;
                    }
                    Pair<Long, String> bookStarPair = Pair.of(book.getId(), star.getLevel());
                    if (!existingRatingStarPairs.contains(bookStarPair)) {
                        BookRatingStar bookRatingStar = new BookRatingStar(book, star, entry.getValue());
                        bookRatingStarsToSave.add(bookRatingStar);
                        existingRatingStarPairs.add(bookStarPair);
                        book.addBookRatingStar(bookRatingStar);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing row for BookID: " + row.getBookID() + ". Error: " + e.getMessage());
            }
        }

        if (!bookRatingStarsToSave.isEmpty()) {
            ratingStarRepository.saveAll(bookRatingStarsToSave);
        }
    }
}
