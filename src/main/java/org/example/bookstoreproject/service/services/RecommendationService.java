package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final BookRepository bookRepository;

    public Page<BookDTO> recommendBooksByGenres(Long bookId, Pageable pageable) {
        return bookRepository.findRecommendedBooksByGenre(bookId, pageable);

    }
}
