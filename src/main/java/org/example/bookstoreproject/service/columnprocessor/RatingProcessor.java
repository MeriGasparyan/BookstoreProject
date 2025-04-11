package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.RatingRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.FloatFormatter;
import org.example.bookstoreproject.service.format.IntegerFormatter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
@Order(10)
public class RatingProcessor implements CSVColumnProcessor {

    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final IntegerFormatter integerFormatter;
    private final FloatFormatter floatFormatter;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Book> bookMap = new HashMap<>();
        List<Rating> ratingsToSave = new ArrayList<>();
        List<Rating> existingRatings = ratingRepository.findAll();
        List<Book> existingBooks = bookRepository.findAll();
        Map<String, Rating> existingRatingsMap = new HashMap<>();
        for(Rating rating: existingRatings){
            existingRatingsMap.put(rating.getBook().getBookID(), rating);
        }
        for(Book book: existingBooks){
            bookMap.put(book.getBookID(), book);
        }
        for (CSVRow row : data) {
            try {
                Float ratingValue = floatFormatter.getFloat(row.getRating());
                Integer bbeScore = integerFormatter.getInt(row.getBbeScore());
                Integer bbeVotes = integerFormatter.getInt(row.getBbeVotes());
                String bookID = row.getBookID().trim();

                Book book = bookMap.get(bookID);

                if (book == null) {
                    System.out.println("No book found for BookID: " + row.getBookID());
                    continue;
                }

                if(existingRatingsMap.containsKey(bookID)){
                    continue;
                }
                Rating rating = new Rating(ratingValue, bbeScore, bbeVotes, book);
                existingRatingsMap.put(rating.getBook().getBookID(), rating);
                ratingsToSave.add(rating);

            } catch (Exception e) {
                System.err.println("Error processing row for BookID: " + row.getBookID() + ". Error: " + e.getMessage());
            }
        }

        if (!ratingsToSave.isEmpty()) {
            ratingRepository.saveAll(ratingsToSave);
        }
    }
}
