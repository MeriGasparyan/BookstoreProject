package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.Rating;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.RatingRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.FloatFormatter;
import org.example.bookstoreproject.service.format.IntegerFormatter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Order(9)
public class RatingProcessor implements CSVColumnProcessor {
    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final IntegerFormatter integerFormatter;
    private final FloatFormatter floatFormatter;

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            try {
                Float rating = floatFormatter.getFloat(row.getRating());
                Optional<Book> existingBook =
                        bookRepository.findByBookID(row.getBookID().trim());
                if(existingBook.isEmpty()) {
                    System.out.println("No such book found " + row.getIsbn().trim());
                    continue;
                }
                boolean alreadyExists = ratingRepository
                        .existsByRatingAndBook_Id(rating, existingBook.get().getId());

                if (alreadyExists) {
                    System.out.println("Book already exists: ISBN = "
                            + row.getIsbn().trim() + ", Title = " + row.getTitle().trim());
                }
                else {
                    Rating newRating = new Rating();
                }
            }
            catch (Exception e) {
                System.err.println("Error processing row with ISBN: " + row.getIsbn() + ". Error: " + e.getMessage());

            }
    }
    }
}
