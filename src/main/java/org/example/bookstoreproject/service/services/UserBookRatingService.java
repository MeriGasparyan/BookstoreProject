package org.example.bookstoreproject.service.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.RatingStarRepository;
import org.example.bookstoreproject.persistance.repository.StarRepository;
import org.example.bookstoreproject.persistance.repository.UserBookRatingRepository;
import org.example.bookstoreproject.service.dto.RatingDTO;
import org.example.bookstoreproject.service.dto.RatingResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBookRatingService {

    private final UserBookRatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final StarRepository starRepository;
    private final RatingStarRepository bookRatingStarRepository;

    @Transactional
    public UserBookRating rateBook(User user, Long bookID, RatingDTO ratingDTO) {

        UserBookRating userBookRating = ratingRepository
                .findByUserIdAndBookId(user.getId(), bookID).orElse(null);
        Book book = bookRepository.findById(bookID)
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookID + " not found."));

        RatingStarNumber ratingEnum = RatingStarNumber.fromInt(ratingDTO.getStar());
        Star star = starRepository.findByLevel(ratingEnum)
                .orElseThrow(() -> new IllegalArgumentException("Star level '" + ratingEnum + "' not found."));


        if (userBookRating != null) {
            if (!userBookRating.getStar().equals(star)) {
                BookRatingStar bookRatingStar = bookRatingStarRepository.findByBookAndStar(userBookRating.getBook(), userBookRating.getStar()).orElseThrow();
                bookRatingStar.setNumRating(bookRatingStar.getNumRating() - 1);
                bookRatingStarRepository.save(bookRatingStar);
                userBookRating.setStar(star);
                BookRatingStar otherBookRatingStar = bookRatingStarRepository.findByBookAndStar(book, star).orElseThrow();
                otherBookRatingStar.setNumRating(otherBookRatingStar.getNumRating() + 1);
                bookRatingStarRepository.save(otherBookRatingStar);
            }
            return userBookRating;
        }

        userBookRating = new UserBookRating();
        userBookRating.setUser(user);
        userBookRating.setBook(book);
        userBookRating.setStar(star);
        userBookRating.setReview(ratingDTO.getReview());
        UserBookRating savedUserBookRating = ratingRepository.save(userBookRating);
        BookRatingStar otherBookRatingStar = bookRatingStarRepository.findByBookAndStar(book, star).orElseThrow();
        otherBookRatingStar.setNumRating(otherBookRatingStar.getNumRating() + 1);

        return savedUserBookRating;
    }

    public Optional<UserBookRating> getRatingByUserAndBook(Long userId, Long bookId) {
        return ratingRepository.findByUserIdAndBookId(userId, bookId);
    }

    @Transactional
    public void deleteRating(Long userId, Long bookId) {
        ratingRepository.deleteByUserIdAndBookId(userId, bookId);
    }


    public Page<RatingResponseDTO> getReviewsByBookId(Long bookId, Pageable pageable) {
        Page<UserBookRating> ratings = ratingRepository.findByBookIdAndReviewIsNotNull(bookId, pageable);
        return ratings.map(RatingResponseDTO::fromEntity);
    }

    public void nullifyReviewText(Long reviewId, Long userId) {
        UserBookRating rating = ratingRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (!rating.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to remove this review");
        }

        rating.setReview(null);
        ratingRepository.save(rating);
    }

    public void nullifyReviewText(Long reviewId) {
        UserBookRating rating = ratingRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        rating.setReview(null);
        ratingRepository.save(rating);
    }


}
