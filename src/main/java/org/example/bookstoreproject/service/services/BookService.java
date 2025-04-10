package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.dto.BookCreateRequestDTO;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookAwardRepository bookAwardRepository;
    private final BookCharacterRepository bookCharacterRepository;
    private final BookGenreRepository bookGenreRepository;
    private final BookSettingRepository bookSettingRepository;
    private final AuthorRepository authorRepository;
    private final FormatRepository formatRepository;
    private final LanguageRepository languageRepository;
    private final SeriesRepository seriesRepository;
    private final PublisherRepository publisherRepository;

    @Transactional(readOnly = true)
    public BookDTO getBookByTitle(String title) {
        Optional<Book> existingBook = bookRepository.findByTitle(title);
        return existingBook.map(book -> BookDTO.fromEntity(
                book,
                bookAuthorRepository,
                bookAwardRepository,
                bookCharacterRepository,
                bookGenreRepository,
                bookSettingRepository
        )).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByAuthorName(String authorName) {
        Optional<Author> authorOptional = authorRepository.findByName(authorName);
        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            return bookAuthorRepository.findByAuthorId(author.getId()).stream()
                    .map(BookAuthor::getBook)
                    .map(book -> BookDTO.fromEntity(
                            book,
                            bookAuthorRepository,
                            bookAwardRepository,
                            bookCharacterRepository,
                            bookGenreRepository,
                            bookSettingRepository
                    ))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Transactional
    public void addBook(BookCreateRequestDTO createRequest) {
        if (bookRepository.findByBookID(createRequest.getBookID()).isPresent()) {
            throw new IllegalArgumentException("Book with ID " + createRequest.getBookID() + " already exists.");
        }

        LanguageEntity language = languageRepository.findByLanguage(Language.fromString(createRequest.getLanguage()).name()).orElse(null);
        FormatEntity format = formatRepository.findByFormat(Format.fromString(createRequest.getFormat()).name()).orElse(null);
        Publisher publisher = publisherRepository.findByName(createRequest.getPublisherName()).orElseGet(() -> publisherRepository.save(new Publisher(createRequest.getPublisherName())));
        Series series = seriesRepository.findByTitle(createRequest.getSeriesTitle()).orElseGet(() -> seriesRepository.save(new Series(createRequest.getSeriesTitle())));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as needed
        java.util.Date publishDate = null;
        java.util.Date firstPublishDate = null;
        try {
            if (createRequest.getPublishDate() != null && !createRequest.getPublishDate().isEmpty()) {
                publishDate = dateFormat.parse(createRequest.getPublishDate());
            }
            if (createRequest.getFirstPublishDate() != null && !createRequest.getFirstPublishDate().isEmpty()) {
                firstPublishDate = dateFormat.parse(createRequest.getFirstPublishDate());
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }

        Book newBook = new Book(
                createRequest.getTitle(),
                createRequest.getBookID(),
                language,
                createRequest.getIsbn(),
                format,
                createRequest.getPages() != null ? createRequest.getPages() : null,
                createRequest.getPrice() != null ? createRequest.getPrice() : null,
                publishDate,
                firstPublishDate,
                publisher,
                series
        );

        bookRepository.save(newBook);
    }
}