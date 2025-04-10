package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.Rating;
import org.example.bookstoreproject.persistance.entry.RatingStar;
import org.example.bookstoreproject.persistance.entry.Star;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.RatingRepository;
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
    private final RatingRepository ratingRepository;
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

        Rating rating = ratingRepository.findByBook_Id(book.getId())
                .orElseGet(() -> {
                    Rating newRating = new Rating(0.0f, 0, 0, book);
                    return ratingRepository.save(newRating);
                });

        Optional<RatingStar> existingRatingStar = ratingStarRepository.findByRatingAndStar(rating, star);

        if (existingRatingStar.isPresent()) {
            RatingStar ratingStar = existingRatingStar.get();
            ratingStar.setNumRating(ratingStar.getNumRating() + 1);
            ratingStarRepository.save(ratingStar);
        } else {
            RatingStar newRatingStar = new RatingStar(rating, star, 1L);
            ratingStarRepository.save(newRatingStar);
        }

        recalculateAverageRating(rating);
    }

    private void recalculateAverageRating(Rating rating) {
        long totalStars = 0;
        long totalRatings = 0;

        for (RatingStar ratingStar : ratingStarRepository.findByRating(rating)) {
            totalStars += getNumericStarValue(ratingStar.getStar().getLevel()) * ratingStar.getNumRating();
            totalRatings += ratingStar.getNumRating();
        }

        if (totalRatings > 0) {
            float averageRating = (float) totalStars / totalRatings;
            rating.setRating(averageRating);
            ratingRepository.save(rating);
        } else {
            rating.setRating(0.0f); // Or handle as appropriate if no ratings yet
            ratingRepository.save(rating);
        }
    }

    private int getNumericStarValue(String starLevel) {
        try {
            return Integer.parseInt(starLevel.split("_")[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Handle potential errors if the level format is unexpected
            return 0; // Or throw an exception
        }
    }
}