package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public BookDTO getBookByTitle(String title) {
        Optional<Book> existingBook = bookRepository.findByTitle(title);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            return new BookDTO(
                    book.getTitle(),
                    book.getLanguage() != null ? book.getLanguage() : null, // handle null
                    book.getIsbn(),
                    book.getFormat() != null ? book.getFormat() : null, // handle null
                    book.getPages(),
                    book.getPrice(),
                    book.getPublishDate(),
                    book.getFirstPublishDate()
            );
        }
        return null;
    }
}