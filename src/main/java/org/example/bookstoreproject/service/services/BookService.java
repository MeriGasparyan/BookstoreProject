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

import java.util.Date;
import java.util.List;
import java.util.Map;
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

        Language language = Language.fromString(createRequest.getLanguage());
        Format format = Format.fromString(createRequest.getFormat());
        Publisher publisher = publisherRepository.findByName(createRequest.getPublisher()).orElseGet(() -> publisherRepository.save(new Publisher(createRequest.getPublisher())));
        Series series = seriesRepository.findByTitle(createRequest.getSeries()).orElseGet(() -> seriesRepository.save(new Series(createRequest.getSeries())));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date publishDate = null, firstPublishDate = null;
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
        Integer pages = Integer.parseInt(createRequest.getPages());
        Float price = Float.parseFloat(createRequest.getPrice());
        Integer bbeScore = integerFormatter.getInt(createRequest.getBbeScore());
        Integer bbeVotes = integerFormatter.getInt(createRequest.getBbeVotes());
        Book book = new Book();
        book.setBookID(createRequest.getBookID());
        book.setFormat(format);
        book.setLanguage(language);
        book.setPublisher(publisher);
        book.setSeries(series);
        book.setPrice(price);
        book.setBbeScore(bbeScore);
        book.setBbeVotes(bbeVotes);
        book.setTitle(createRequest.getTitle());
        book.setIsbn(createRequest.getIsbn());
        book.setPages(pages);
        book.setPublisher(publisher);
        book.setSeries(series);
        book.setPublishDate(publishDate);
        book.setFirstPublishDate(firstPublishDate);
        bookRepository.save(book);


        Map<RatingStarNumber, Long> starMap = ratingByStarFormatter.formatRatingsByStar(createRequest.getRatingsByStar());
        for (Map.Entry<RatingStarNumber, Long> entry : starMap.entrySet()) {
            Star star = starRepository.findByLevel(entry.getKey().name())
                    .orElseThrow(() -> new IllegalArgumentException("Star level not found: " + entry.getKey().name()));
            ratingStarRepository.save(new BookRatingStar(book, star, entry.getValue()));
        }

        for (String authorName : createRequest.getAuthor().split(",")) {
            Author author = authorRepository.findByName(authorName.trim()).orElseGet(() -> authorRepository.save(new Author(authorName.trim())));
            bookAuthorRepository.save(new BookAuthor(book, author));
        }

        for (String characterName : Optional.ofNullable(createRequest.getCharacters()).orElse("").split(",")) {
            if (!characterName.trim().isEmpty()) {
                Character character = characterRepository.findByName(characterName.trim())
                        .orElseGet(() -> characterRepository.save(new Character(characterName.trim())));
                bookCharacterRepository.save(new BookCharacter(book, character));
            }
        }

        for (String genreName : Optional.ofNullable(createRequest.getGenres()).orElse("").split(",")) {
            if (!genreName.trim().isEmpty()) {
                Genre genre = genreRepository.findByName(genreName.trim())
                        .orElseGet(() -> genreRepository.save(new Genre(genreName.trim())));
                bookGenreRepository.save(new BookGenre(book, genre));
            }
        }

        for (String awardName : Optional.ofNullable(createRequest.getAwards()).orElse("").split(",")) {
            if (!awardName.trim().isEmpty()) {
                Award award = awardRepository.findByTitle(awardName.trim())
                        .orElseGet(() -> awardRepository.save(new Award(awardName.trim())));
                bookAwardRepository.save(new BookAward(book, award));
            }
        }
        for (String settingName : Optional.ofNullable(createRequest.getSettings()).orElse("").split(",")) {
            if (!settingName.trim().isEmpty()) {
                Setting setting = settingRepository.findByName(settingName.trim())
                        .orElseGet(() -> settingRepository.save(new Setting(settingName.trim())));
                bookSettingRepository.save(new BookSetting(book, setting));
            }
        }
    }

}