package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.dto.BookCreateRequestDTO;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.example.bookstoreproject.service.dto.BookUpdateRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final SeriesRepository seriesRepository;
    private final PublisherRepository publisherRepository;
    private final CharacterRepository characterRepository;
    private final GenreRepository genreRepository;
    private final AwardRepository awardRepository;
    private final SettingRepository settingRepository;

    @Transactional
    public void addBook(BookCreateRequestDTO createRequest) {
        if (bookRepository.findByBookID(createRequest.getBookID()).isPresent()) {
            throw new IllegalArgumentException("Book with ID " + createRequest.getBookID() + " already exists.");
        }

        Book book = createRequest.createBookEntity(createRequest, publisherRepository, seriesRepository);
        bookRepository.save(book);
        addAuthors(book, createRequest.getAuthor());
        addCharacters(book, createRequest.getCharacters());
        addGenres(book, createRequest.getGenres());
        addAwards(book, createRequest.getAwards());
        addSettings(book, createRequest.getSettings());
    }

    private void addAuthors(Book book, List<String> authors) {
        Optional.ofNullable(authors).orElse(List.of()).stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .forEach(name -> {
                    Author author = authorRepository.findByName(name)
                            .orElseGet(() -> authorRepository.save(new Author(name)));
                    book.addBookAuthor(new BookAuthor(book, author));
                });
    }


    private void addCharacters(Book book, List<String> characters) {
        Optional.ofNullable(characters).orElse(List.of()).stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .forEach(name -> {
                    Character character = characterRepository.findByName(name)
                            .orElseGet(() -> characterRepository.save(new Character(name)));
                    book.addBookCharacter(new BookCharacter(book, character));
                });
    }

    private void addGenres(Book book, List<String> genres) {
        Optional.ofNullable(genres).orElse(List.of()).stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .forEach(name -> {
                    Genre genre = genreRepository.findByName(name)
                            .orElseGet(() -> genreRepository.save(new Genre(name)));
                    book.addBookGenre(new BookGenre(book, genre));
                });
    }

    private void addAwards(Book book, List<String> awards) {
        Optional.ofNullable(awards).orElse(List.of()).stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .forEach(name -> {
                    Award award = awardRepository.findByTitle(name)
                            .orElseGet(() -> awardRepository.save(new Award(name)));
                    book.addBookAward(new BookAward(book, award));
                });
    }

    private void addSettings(Book book, List<String> settings) {
        Optional.ofNullable(settings).orElse(List.of()).stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .forEach(name -> {
                    Setting setting = settingRepository.findByName(name)
                            .orElseGet(() -> settingRepository.save(new Setting(name)));
                    book.addBookSetting(new BookSetting(book, setting));
                });
    }

    public List<BookDTO> searchBooks(BookSearchCriteria criteria, int size) {
        String title = criteria.getTitle() != null ? criteria.getTitle().toLowerCase() : null;

        List<Long> authorIds = criteria.getAuthorIds() != null ? criteria.getAuthorIds() : null;
        List<Long> genreIds = criteria.getGenreIds() != null ? criteria.getGenreIds() : null;
        Language language = criteria.getLanguage() != null ? criteria.getLanguage() : null;
        List<Long> publisherIds = criteria.getPublisherIds() != null ? criteria.getPublisherIds() : null;
        List<Long> seriesIds = criteria.getSeriesIds() != null ? criteria.getSeriesIds() : null;
        List<Long> awardIds = criteria.getAwardIds() != null ? criteria.getAwardIds() : null;
        List<Long> characterIds = criteria.getCharacterIds() != null ? criteria.getCharacterIds() : null;
        List<Long> settingIds = criteria.getSettingIds() != null ? criteria.getSettingIds() : null;
        int authorIdsSize = (authorIds != null) ? authorIds.size() : 0;
        int genreIdsSize = (genreIds != null) ? genreIds.size() : 0;
        int publisherIdsSize = (publisherIds != null) ? publisherIds.size() : 0;
        int seriesIdsSize = (seriesIds != null) ? seriesIds.size() : 0;
        int awardIdsSize = (awardIds != null) ? awardIds.size() : 0;
        int characterIdsSize = (characterIds != null) ? characterIds.size() : 0;
        int settingsSize = (settingIds != null) ? settingIds.size() : 0;

        Pageable pageable = PageRequest.of(criteria.getPage(), size);
        List<Book> result = bookRepository.searchBooks(
                title,
                authorIds,
                genreIds,
                publisherIds,
                seriesIds,
                awardIds,
                characterIds,
                settingIds,
                authorIdsSize,
                genreIdsSize,
                publisherIdsSize,
                seriesIdsSize,
                awardIdsSize,
                characterIdsSize,
                settingsSize,
                language,
                pageable
        );

        return result.stream()
                .map(BookDTO::fromEntity)
                .toList();
    }


    @Transactional
    public void updateBook(Long id, BookUpdateRequestDTO updateRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + id + " not found"));

        if (updateRequest.getTitle() != null) book.setTitle(updateRequest.getTitle());
        if (updateRequest.getIsbn() != null) book.setIsbn(updateRequest.getIsbn());
        if (updateRequest.getPages() != null) book.setPages(updateRequest.getPages());
        if (updateRequest.getPrice() != null) book.setPrice(updateRequest.getPrice());
        if (updateRequest.getPublishDate() != null) book.setPublishDate(updateRequest.getPublishDate());
        if (updateRequest.getFirstPublishDate() != null) book.setFirstPublishDate(updateRequest.getFirstPublishDate());
        if (updateRequest.getLanguage() != null) book.setLanguage(updateRequest.getLanguage());
        if (updateRequest.getFormat() != null) book.setFormat(updateRequest.getFormat());

        if (updateRequest.getPublisherName() != null) {
            Publisher publisher = publisherRepository.findByName(updateRequest.getPublisherName())
                    .orElseGet(() -> publisherRepository.save(new Publisher(updateRequest.getPublisherName())));
            book.setPublisher(publisher);
        }

        if (updateRequest.getSeriesTitle() != null) {
            Series series = seriesRepository.findByTitle(updateRequest.getSeriesTitle())
                    .orElseGet(() -> seriesRepository.save(new Series(updateRequest.getSeriesTitle())));
            book.setSeries(series);
        }

        book.clearBookAuthors();
        addAuthors(book, updateRequest.getAuthors());

        book.clearBookCharacters();
        addCharacters(book, updateRequest.getCharacters());

        book.clearBookGenres();
        addGenres(book, updateRequest.getGenres());

        book.clearBookAwards();
        addAwards(book, updateRequest.getAwards());

        book.clearBookSettings();
        addSettings(book, updateRequest.getSettings());

        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + id + " not found"));

        book.clearBookAuthors();
        book.clearBookAwards();
        book.clearBookCharacters();
        book.clearBookGenres();
        book.clearBookSettings();

        bookRepository.delete(book);
    }


}