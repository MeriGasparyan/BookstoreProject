package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.dto.BookCreateRequestDTO;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.example.bookstoreproject.service.dto.BookSearchRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<BookDTO> searchBooks(BookSearchRequestDTO request) {
        Set<Book> result = null;

        if (request.getTitle() != null) {
            result = intersect(result, bookRepository.findByTitleContainingIgnoreCase(request.getTitle()));
        }
        if (request.getAuthor() != null) {
            result = intersect(result, bookRepository.findByBookAuthors_Author_NameContainingIgnoreCase(request.getAuthor()));
        }
        if (request.getGenre() != null) {
            result = intersect(result, bookRepository.findByBookGenres_Genre_NameContainingIgnoreCase(request.getGenre()));
        }
        if (request.getLanguage() != null) {
            result = intersect(result, bookRepository.findByLanguage(Language.fromString(request.getLanguage())));
        }
        if (request.getPublisher() != null) {
            result = intersect(result, bookRepository.findByPublisher_NameContainingIgnoreCase(request.getPublisher()));
        }
        if (request.getSeries() != null) {
            result = intersect(result, bookRepository.findBySeries_TitleContainingIgnoreCase(request.getSeries()));
        }
        if (request.getAward() != null) {
            result = intersect(result, bookRepository.findByBookAwards_Award_TitleContainingIgnoreCase(request.getAward()));
        }
        if (request.getCharacter() != null) {
            result = intersect(result, bookRepository.findByBookCharacters_Character_NameContainingIgnoreCase(request.getCharacter()));
        }
        if (request.getSetting() != null) {
            result = intersect(result, bookRepository.findByBookSettings_Setting_NameContainingIgnoreCase(request.getSetting()));
        }

        if (result == null) {
            result = new HashSet<>(bookRepository.findAll());
        }

        return result.stream()
                .limit(20)
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private Set<Book> intersect(Set<Book> base, Set<Book> newSet) {
        if (base == null) return new HashSet<>(newSet);
        base.retainAll(newSet);
        return base;
    }

}