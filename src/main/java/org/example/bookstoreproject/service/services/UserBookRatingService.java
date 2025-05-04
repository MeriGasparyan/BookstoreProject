package org.example.bookstoreproject.service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.Star;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.StarRepository;
import org.example.bookstoreproject.persistance.repository.UserBookRatingRepository;
import org.example.bookstoreproject.service.dto.RatingDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBookRatingService {

    private final UserBookRatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final StarRepository starRepository;

    @Transactional
    public UserBookRating rateBook(User user, Long bookID, RatingDTO ratingDTO) {
        UserBookRating userBookRating = ratingRepository
                .findByUserIdAndBookId(user.getId(), bookID)
                .orElseGet(UserBookRating::new);

        Book book = bookRepository.findById(bookID)
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookID + " not found."));

        RatingStarNumber ratingEnum = RatingStarNumber.fromInt(ratingDTO.getStar());
        Star star = starRepository.findByLevel(ratingEnum)
                .orElseThrow(() -> new IllegalArgumentException("Star level '" + ratingEnum + "' not found."));


        userBookRating.setUser(user);
        userBookRating.setBook(book);
        userBookRating.setStar(star);

        return ratingRepository.save(userBookRating);
    }

    public Optional<UserBookRating> getRatingByUserAndBook(Long userId, Long bookId) {
        return ratingRepository.findByUserIdAndBookId(userId, bookId);
    }

    @Transactional
    public void deleteRating(Long userId, Long bookId) {
        ratingRepository.deleteByUserIdAndBookId(userId, bookId);
    }
}
