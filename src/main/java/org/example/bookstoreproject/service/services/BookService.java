package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookAuthor;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.persistance.repository.BookAuthorRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final AuthorRepository authorRepository;

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

    public List<BookDTO> getBooksByAuthor(Long authorId) {
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByAuthorId(authorId);
        return bookAuthors.stream()
                .map(bookAuthor -> {
                    Book book = bookAuthor.getBook();
                    return new BookDTO(
                            book.getTitle(),
                            book.getLanguage(),
                            book.getIsbn(),
                            book.getFormat(),
                            book.getPages(),
                            book.getPrice(),
                            book.getPublishDate(),
                            book.getFirstPublishDate()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksByAuthorName(String authorName) {
        Optional<Author> authorOpt = authorRepository.findByName(authorName);
        if (authorOpt.isPresent()) {
            Author author = authorOpt.get();
            List<BookAuthor> bookAuthors = bookAuthorRepository.findByAuthorId(author.getId());
            return bookAuthors.stream()
                    .map(bookAuthor -> {
                        Book book = bookAuthor.getBook();
                        return new BookDTO(
                                book.getTitle(),
                                book.getLanguage(),
                                book.getIsbn(),
                                book.getFormat(),
                                book.getPages(),
                                book.getPrice(),
                                book.getPublishDate(),
                                book.getFirstPublishDate()
                        );
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}