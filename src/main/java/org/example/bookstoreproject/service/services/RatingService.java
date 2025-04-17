package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookRatingStar;
import org.example.bookstoreproject.persistance.entry.Star;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.RatingStarRepository;
import org.example.bookstoreproject.persistance.repository.StarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.bookstoreproject.enums.RatingStarNumber;


@Service
@AllArgsConstructor
public class RatingService {
    private final BookRepository bookRepository;
    private final RatingStarRepository ratingStarRepository;
    private final StarRepository starRepository;

    @Transactional
    public void rateBook(String bookID, Integer starValue) {
        Book book = bookRepository.findByBookID(bookID)
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookID + " not found."));

        RatingStarNumber ratingEnum = RatingStarNumber.fromInt(starValue); // create helper
        Star star = starRepository.findByLevel(ratingEnum.name())
                .orElseThrow(() -> new IllegalArgumentException("Star level '" + ratingEnum + "' not found."));

        BookRatingStar ratingStar = ratingStarRepository.findByBookAndStar(book, star)
                .orElseGet(() -> new BookRatingStar(book, star, 0L));

        ratingStar.setNumRating(ratingStar.getNumRating() + 1);
        ratingStarRepository.save(ratingStar);
    }

}