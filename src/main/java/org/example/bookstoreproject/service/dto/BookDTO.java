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
    private Long totalNumRatings;

    public static BookDTO fromEntity(Book book) {
        if (book == null) {
            return null;
        }

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
            long totalNumRatings = 0;
            long weightedSum = 0;

            for (BookRatingStar ratingStar : ratingsList) {
                if (ratingStar != null && ratingStar.getStar() != null) {
                    long localNumRating = ratingStar.getNumRating();
                    RatingStarNumber starLevel = ratingStar.getStar().getLevel();
                    dto.ratingStars.put(starLevel, localNumRating);
                    totalNumRatings += localNumRating;
                    weightedSum += (starLevel.ordinal() + 1) * localNumRating;
                }
            }

            dto.setTotalNumRatings(totalNumRatings);
            dto.setAverageRating(totalNumRatings > 0 ? (float) weightedSum / totalNumRatings : 0f);
        } else {
            dto.setTotalNumRatings(0L);
            dto.setAverageRating(0f);
        }

        return dto;
    }
}