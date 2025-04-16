package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.entry.Character;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.dto.BookCreateRequestDTO;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.example.bookstoreproject.service.format.FloatFormatter;
import org.example.bookstoreproject.service.format.IntegerFormatter;
import org.example.bookstoreproject.service.format.RatingByStarFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    private final SeriesRepository seriesRepository;
    private final PublisherRepository publisherRepository;
    private final RatingStarRepository ratingStarRepository;
    private final StarRepository starRepository;
    private final CharacterRepository characterRepository;
    private final GenreRepository genreRepository;
    private final AwardRepository awardRepository;
    private final RatingByStarFormatter ratingByStarFormatter;
    private final IntegerFormatter integerFormatter;
    private final FloatFormatter floatFormatter;
    private final SettingRepository settingRepository;

    @Transactional(readOnly = true)
    public BookDTO getBookByTitle(String title) {
        return bookRepository.findByTitle(title)
                .map(BookDTO::fromEntity)
                .orElse(null);
    }

    @Transactional
    public void addBook(BookCreateRequestDTO createRequest) {
        if (bookRepository.findByBookID(createRequest.getBookID()).isPresent()) {
            throw new IllegalArgumentException("Book with ID " + createRequest.getBookID() + " already exists.");
        }

        Book book = createBookEntity(createRequest);
        bookRepository.save(book);

        addRatingStars(book, createRequest.getRatingsByStar());
        addAuthors(book, createRequest.getAuthor());
        addCharacters(book, createRequest.getCharacters());
        addGenres(book, createRequest.getGenres());
        addAwards(book, createRequest.getAwards());
        addSettings(book, createRequest.getSettings());
    }

    private Book createBookEntity(BookCreateRequestDTO createRequest) {
        Book book = new Book();
        book.setBookID(createRequest.getBookID());
        book.setTitle(createRequest.getTitle());
        book.setIsbn(createRequest.getIsbn());
        book.setLanguage(Language.fromString(createRequest.getLanguage()));
        book.setFormat(Format.fromString(createRequest.getFormat()));
        book.setPages(integerFormatter.getInt(createRequest.getPages()));
        book.setPrice(floatFormatter.getFloat(createRequest.getPrice()));
        book.setBbeScore(integerFormatter.getInt(createRequest.getBbeScore()));
        book.setBbeVotes(integerFormatter.getInt(createRequest.getBbeVotes()));


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (createRequest.getPublishDate() != null && !createRequest.getPublishDate().isEmpty()) {
                book.setPublishDate(dateFormat.parse(createRequest.getPublishDate()));
            }
            if (createRequest.getFirstPublishDate() != null && !createRequest.getFirstPublishDate().isEmpty()) {
                book.setFirstPublishDate(dateFormat.parse(createRequest.getFirstPublishDate()));
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }

        book.setPublisher(
                publisherRepository.findByName(createRequest.getPublisher())
                        .orElseGet(() -> publisherRepository.save(new Publisher(createRequest.getPublisher())))
        );
        book.setSeries(
                seriesRepository.findByTitle(createRequest.getSeries())
                        .orElseGet(() -> seriesRepository.save(new Series(createRequest.getSeries())))
        );

        return book;
    }

    private void addRatingStars(Book book, String ratingsByStar) {
        Map<RatingStarNumber, Long> starMap = ratingByStarFormatter.formatRatingsByStar(ratingsByStar);
        starMap.forEach((starLevel, count) -> {
            Star star = starRepository.findByLevel(starLevel.name())
                    .orElseGet(() -> starRepository.save(new Star(starLevel.name())));
            ratingStarRepository.save(new BookRatingStar(book, star, count));
        });
    }

    private void addAuthors(Book book, String authors) {
        Arrays.stream(Optional.ofNullable(authors).orElse("").split(","))
                .filter(name -> !name.trim().isEmpty())
                .forEach(name -> {
                    Author author = authorRepository.findByName(name.trim())
                            .orElseGet(() -> authorRepository.save(new Author(name.trim())));
                    book.addBookAuthor(new BookAuthor(book, author));
                });
    }

    private void addCharacters(Book book, String characters) {
        Arrays.stream(Optional.ofNullable(characters).orElse("").split(","))
                .filter(name -> !name.trim().isEmpty())
                .forEach(name -> {
                    Character character = characterRepository.findByName(name.trim())
                            .orElseGet(() -> characterRepository.save(new Character(name.trim())));
                    book.addBookCharacter(new BookCharacter(book, character));
                });
    }

    private void addGenres(Book book, String genres) {
        Arrays.stream(Optional.ofNullable(genres).orElse("").split(","))
                .filter(name -> !name.trim().isEmpty())
                .forEach(name -> {
                    Genre genre = genreRepository.findByName(name.trim())
                            .orElseGet(() -> genreRepository.save(new Genre(name.trim())));
                    book.addBookGenre(new BookGenre(book, genre));
                });
    }

    private void addAwards(Book book, String awards) {
        Arrays.stream(Optional.ofNullable(awards).orElse("").split(","))
                .filter(name -> !name.trim().isEmpty())
                .forEach(name -> {
                    Award award = awardRepository.findByTitle(name.trim())
                            .orElseGet(() -> awardRepository.save(new Award(name.trim())));
                    book.addBookAward(new BookAward(book, award));
                });
    }

    private void addSettings(Book book, String settings) {
        Arrays.stream(Optional.ofNullable(settings).orElse("").split(","))
                .filter(name -> !name.trim().isEmpty())
                .forEach(name -> {
                    Setting setting = settingRepository.findByName(name.trim())
                            .orElseGet(() -> settingRepository.save(new Setting(name.trim())));
                    book.addBookSetting(new BookSetting(book, setting));
                });
    }

}