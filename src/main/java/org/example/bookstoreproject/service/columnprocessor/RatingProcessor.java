package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.RatingRepository;
import org.example.bookstoreproject.persistance.repository.RatingStarRepository;
import org.example.bookstoreproject.persistance.repository.StarRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.FloatFormatter;
import org.example.bookstoreproject.service.format.IntegerFormatter;
import org.example.bookstoreproject.service.format.RatingByStarFormatter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

@Component
@AllArgsConstructor
@Order(10)
public class RatingProcessor implements CSVColumnProcessor {
    private final RatingRepository ratingRepository;
    private final RatingStarRepository ratingStarRepository;
    private final BookRepository bookRepository;
    private final IntegerFormatter integerFormatter;
    private final FloatFormatter floatFormatter;
    private final RatingByStarFormatter ratingByStarFormatter;
    private final StarRepository starRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Book> bookCache = new HashMap<>();
        Map<String, Star> starCache = new HashMap<>();
        List<RatingStar> ratingStarsToSave = new ArrayList<>();
        List<Rating> ratingsToSave = new ArrayList<>();

        Set<Pair<Long, String>> existingRatingStarSet = new HashSet<>();

        for (CSVRow row : data) {
            try {
                Float rating = floatFormatter.getFloat(row.getRating());
                Integer bbeScore = integerFormatter.getInt(row.getBbeScore());
                Integer bbeVotes = integerFormatter.getInt(row.getBbeVotes());

                Book book = bookCache.computeIfAbsent(row.getBookID().trim(), bookID -> {
                    return bookRepository.findByBookID(bookID).orElse(null);
                });

                if (book == null) {
                    System.out.println("No such book found " + row.getIsbn().trim());
                    continue;
                }

                boolean alreadyExists = ratingRepository.existsByRatingAndBook_Id(rating, book.getId());
                if (alreadyExists) {
                    System.out.println("Book rating already exists: BookID = " + row.getBookID().trim() + ", Rating = " + row.getRating().trim());
                    continue;
                }

                Rating newRating = new Rating(rating, bbeScore, bbeVotes, book);
                ratingsToSave.add(newRating);

                Map<RatingStarNumber, Long> ratingsByStar = ratingByStarFormatter.formatRatingsByStar(row.getRatingsByStar());
                if (ratingsByStar == null) {
                    continue;
                }

                for (Map.Entry<RatingStarNumber, Long> entry : ratingsByStar.entrySet()) {
                    Star star = starCache.computeIfAbsent(entry.getKey().name(), starLevel -> {
                        return starRepository.findByLevel(starLevel).orElse(null);
                    });

                    if (star == null) {
                        System.out.println("No star found for level: " + entry.getKey().name());
                        continue;
                    }

                    Pair<Long, String> pair = Pair.of(newRating.getId(), star.getLevel());
                    if (!existingRatingStarSet.contains(pair)) {
                        RatingStar newRatingStar = new RatingStar(newRating, star, entry.getValue());
                        ratingStarsToSave.add(newRatingStar);
                        existingRatingStarSet.add(pair);
                    }
                }

            } catch (Exception e) {
                System.err.println("Error processing row with bookID: " + row.getBookID() + ". Error: " + e.getMessage());
            }
        }

        if (!ratingsToSave.isEmpty()) {
            ratingRepository.saveAll(ratingsToSave);
        }
        if (!ratingStarsToSave.isEmpty()) {
            ratingStarRepository.saveAll(ratingStarsToSave);
        }
    }
}
