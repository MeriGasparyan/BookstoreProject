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

import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingService {
    private final BookRepository bookRepository;
    private final RatingStarRepository ratingStarRepository;
    private final StarRepository starRepository;

    @Transactional
    public void rateBook(Long bookId, Integer starValue) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found."));

        String starLevel;
        switch (starValue) {
            case 1:
                starLevel = RatingStarNumber.ONE_STAR.name();
                break;
            case 2:
                starLevel = RatingStarNumber.TWO_STAR.name();
                break;
            case 3:
                starLevel = RatingStarNumber.THREE_STAR.name();
                break;
            case 4:
                starLevel = RatingStarNumber.FOUR_STAR.name();
                break;
            case 5:
                starLevel = RatingStarNumber.FIVE_STAR.name();
                break;
            default:
                throw new IllegalArgumentException("Invalid star value: " + starValue);
        }

        Star star = starRepository.findByLevel(starLevel)
                .orElseThrow(() -> new IllegalArgumentException("Star level '" + starLevel + "' not found."));

        Optional<BookRatingStar> existingRatingStar = ratingStarRepository.findByBookAndStar(book, star);

        if (existingRatingStar.isPresent()) {
            BookRatingStar bookRatingStar = existingRatingStar.get();
            bookRatingStar.setNumRating(bookRatingStar.getNumRating() + 1);
            ratingStarRepository.save(bookRatingStar);
        } else {
            BookRatingStar newBookRatingStar = new BookRatingStar(book, star, 1L);
            ratingStarRepository.save(newBookRatingStar);
        }
    }
}