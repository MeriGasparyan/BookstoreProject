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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
@Order(9)
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
        for (CSVRow row : data) {
            try {
                Float rating = floatFormatter.getFloat(row.getRating());
                Integer bbeScore = integerFormatter.getInt(row.getBbeScore());
                Integer bbeVotes = integerFormatter.getInt(row.getBbeVotes());
                Optional<Book> existingBook =
                        bookRepository.findByBookID(row.getBookID().trim());
                if(existingBook.isEmpty()) {
                    System.out.println("No such book found " + row.getIsbn().trim());
                    continue;
                }
                boolean alreadyExists = ratingRepository
                        .existsByRatingAndBook_Id(rating, existingBook.get().getId());

                if (alreadyExists) {
                    System.out.println("Book  rating already exists: BookID = "
                            + row.getBookID().trim() + ", Rating = " + row.getRating().trim());
                }
                else {
                    Book book = existingBook.get();
                    Rating newRating = new Rating(rating,bbeScore,bbeVotes,book);
                    ratingRepository.save(newRating);
                    Map<RatingStarNumber, Long> ratingsByStar = ratingByStarFormatter.formatRatingsByStar(row.getRatingsByStar());
                    if(ratingsByStar == null) {
                        continue;
                    }
                    for(Map.Entry<RatingStarNumber, Long> entry : ratingsByStar.entrySet()) {

                        Optional<Star> existingStarValue = starRepository.findByLevel(entry.getKey().name());
                        if (existingStarValue.isEmpty()) {
                            System.out.println("No star found for level: " + entry.getKey().name());
                            continue;
                        }
                        RatingStar newRatingStar = new RatingStar(newRating, existingStarValue.get(), entry.getValue());
                        ratingStarRepository.save(newRatingStar);
                        ratingStarRepository.save(newRatingStar);
                    }

                }
            }
            catch (Exception e) {
                System.err.println("Error processing row with bookID: " + row.getBookID() + ". Error: " + e.getMessage());

            }
    }
    }
}
