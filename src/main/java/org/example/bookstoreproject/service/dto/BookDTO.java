package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.enums.RatingStarNumber;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookRatingStar;
import org.example.bookstoreproject.service.utility.RatingCalculator;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private Language language;
    private String isbn;
    private Format format;
    private Integer pages;
    private Float price;
    private Date publishDate;
    private Date firstPublishDate;
    private Map<String, Object> publisher;
    private Map<String, Object> series;
    private List<Map<String, Object>> authors;
    private List<Map<String, Object>> awards;
    private List<Map<String, Object>> characters;
    private List<Map<String, Object>> genres;
    private List<Map<String, Object>> settings;
    private Map<RatingStarNumber, Long> ratingStars = new HashMap<>();
    private Float averageRating;
    private Integer likedPercentage;
    private Long totalNumRatings;

    public BookDTO(Book book) {
        BookDTO dto = fromEntity(book);

        this.id = dto.id;
        this.title = dto.title;
        this.language = dto.language;
        this.isbn = dto.isbn;
        this.format = dto.format;
        this.pages = dto.pages;
        this.price = dto.price;
        this.publishDate = dto.publishDate;
        this.firstPublishDate = dto.firstPublishDate;
        this.publisher = dto.publisher;
        this.series = dto.series;
        this.authors = dto.authors;
        this.awards = dto.awards;
        this.characters = dto.characters;
        this.genres = dto.genres;
        this.settings = dto.settings;
        this.ratingStars = dto.ratingStars;
        this.averageRating = dto.averageRating;
        this.likedPercentage = dto.likedPercentage;
        this.totalNumRatings = dto.totalNumRatings;
    }
    public static BookDTO fromEntity(Book book) {
        if (book == null) {
            return null;
        }
        System.out.println(1111);
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPages(book.getPages());
        dto.setPrice(book.getPrice());
        dto.setPublishDate(book.getPublishDate());
        dto.setFirstPublishDate(book.getFirstPublishDate());
        dto.setLanguage(book.getLanguage());
        dto.setFormat(book.getFormat());

        dto.setPublisher(book.getPublisher() != null ?
                Map.of("id", book.getPublisher().getId(), "name", book.getPublisher().getName()) :
                null);

        dto.setSeries(book.getSeries() != null ?
                Map.of("id", book.getSeries().getId(), "title", book.getSeries().getTitle()) :
                null);

        dto.setAuthors(book.getBookAuthors() != null ?
                book.getBookAuthors().stream()
                        .map(ba -> Map.<String, Object>of(
                                "id", ba.getAuthor().getId(),
                                "name", ba.getAuthor().getName()))
                        .collect(Collectors.toList()) :
                null);

        dto.setAwards(book.getBookAwards() != null ?
                book.getBookAwards().stream()
                        .map(ba -> Map.<String, Object>of(
                                "id", ba.getAward().getId(),
                                "title", ba.getAward().getTitle()))
                        .collect(Collectors.toList()) :
                null);

        dto.setCharacters(book.getBookCharacters() != null ?
                book.getBookCharacters().stream()
                        .map(bc -> Map.<String, Object>of(
                                "id", bc.getCharacter().getId(),
                                "name", bc.getCharacter().getName()))
                        .collect(Collectors.toList()) :
                null);

        dto.setGenres(book.getBookGenres() != null ?
                book.getBookGenres().stream()
                        .map(bg -> Map.<String, Object>of(
                                "id", bg.getGenre().getId(),
                                "name", bg.getGenre().getName()))
                        .collect(Collectors.toList()) :
                null);

        dto.setSettings(book.getBookSettings() != null ?
                book.getBookSettings().stream()
                        .map(bs -> Map.<String, Object>of(
                                "id", bs.getSetting().getId(),
                                "name", bs.getSetting().getName()))
                        .collect(Collectors.toList()) :
                null);

        if (book.getBookRatingStars() != null) {
            List<BookRatingStar> ratingsList = book.getBookRatingStars().stream().toList();
            long totalNumRatings = RatingCalculator.calculateTotalRatingCount(ratingsList);
            int likedPercentage = RatingCalculator.calculateLikedPercentage(ratingsList);
            float averageRating = RatingCalculator.calculateAverageRating(ratingsList);

            for (BookRatingStar ratingStar : ratingsList) {
                if (ratingStar != null && ratingStar.getStar() != null) {
                    long localNumRating = ratingStar.getNumRating();
                    RatingStarNumber starLevel = ratingStar.getStar().getLevel();
                    dto.getRatingStars().put(starLevel, localNumRating);
                }
            }

            dto.setTotalNumRatings(totalNumRatings);
            dto.setAverageRating(averageRating);
            dto.setLikedPercentage(likedPercentage);
        } else {
            dto.setTotalNumRatings(0L);
            dto.setAverageRating(0f);
            dto.setLikedPercentage(0);
        }

        return dto;
    }
    public String toString(){
        return this.title;
    }
}