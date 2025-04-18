package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookRatingStar;
import org.example.bookstoreproject.persistance.repository.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String title;
    private Language language;
    private String isbn;
    private Format format;
    private Integer pages;
    private Float price;
    private Date publishDate;
    private Date firstPublishDate;
    private String publisherName;
    private String seriesTitle;
    private List<String> authors;
    private List<String> awards;
    private List<String> characters;
    private List<String> genres;
    private List<String> settings;
    private Map<String, Long> ratingStars;
    private Float averageRating;
    private Long totalNumRatings;

    public static BookDTO fromEntity(Book book) {
        BookDTO dto = new BookDTO();
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPages(book.getPages());
        dto.setPrice(book.getPrice());
        dto.setPublishDate(book.getPublishDate());
        dto.setFirstPublishDate(book.getFirstPublishDate());
        dto.setLanguage(book.getLanguage());
        dto.setFormat(book.getFormat());
        if (book.getPublisher() != null) {
            dto.setPublisherName(book.getPublisher().getName());
        }
        if (book.getSeries() != null) {
            dto.setSeriesTitle(book.getSeries().getTitle());
        }

        dto.setAuthors(book.getBookAuthors().stream()
                .map(ba -> ba.getAuthor().getName())
                .collect(Collectors.toList()));

        dto.setAwards(book.getBookAwards().stream()
                .map(ba -> ba.getAward().getTitle())
                .collect(Collectors.toList()));

        dto.setCharacters(book.getBookCharacters().stream()
                .map(bc -> bc.getCharacter().getName())
                .collect(Collectors.toList()));

        dto.setGenres(book.getBookGenres().stream()
                .map(bg -> bg.getGenre().getName())
                .collect(Collectors.toList()));

        dto.setSettings(book.getBookSettings().stream()
                .map(bs -> bs.getSetting().getName())
                .collect(Collectors.toList()));

        List<BookRatingStar> ratingsList= book.getBookRatingStars().stream().toList();
        dto.ratingStars = new HashMap<>();
        long totalNumRatings = 0;
        long weightedSum = 0;
        for (BookRatingStar ratingStar : ratingsList) {
            long localNumRating = ratingStar.getNumRating();
            String starLevel = ratingStar.getStar().getLevel();
            dto.ratingStars.put(starLevel, localNumRating);
            totalNumRatings += localNumRating;
            weightedSum += (RatingStarNumber.fromString(starLevel).ordinal() + 1) * localNumRating;
        }
        dto.setTotalNumRatings(totalNumRatings);
        dto.setAverageRating((float)weightedSum / totalNumRatings);

        return dto;
    }
}
