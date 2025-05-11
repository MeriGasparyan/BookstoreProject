package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.entity.Character;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.dto.BookCreateRequestDTO;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.example.bookstoreproject.service.dto.BookUpdateRequestDTO;
import org.example.bookstoreproject.service.dto.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final SeriesRepository seriesRepository;
    private final PublisherRepository publisherRepository;
    private final CharacterRepository characterRepository;
    private final GenreRepository genreRepository;
    private final AwardRepository awardRepository;
    private final SettingRepository settingRepository;

    @Transactional
    public BookDTO addBook(BookCreateRequestDTO createRequest) {
        if (bookRepository.findByBookID(createRequest.getBookID()).isPresent()) {
            throw new IllegalArgumentException("Book with ID " + createRequest.getBookID() + " already exists.");
        }

        Book book = createRequest.createBookEntity(createRequest, publisherRepository, seriesRepository);
        bookRepository.save(book);
        return BookDTO.fromEntity(book);
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

    public PageResponseDto<BookDTO> searchBooks(BookSearchCriteria criteria, Pageable pageable) {
        Page<BookDTO> result = bookRepository.searchBooks(
                criteria,
                pageable
        );
        for (BookDTO bookDTO : result) {
            System.out.println(bookDTO);
        }
        return PageResponseDto.from(result);
    }


    @Transactional
    public Book updateBook(Long id, BookUpdateRequestDTO updateRequest) {
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

        return bookRepository.save(book);
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