package org.example.bookstoreproject.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.Publisher;
import org.example.bookstoreproject.persistance.entity.Series;
import org.example.bookstoreproject.persistance.repository.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BookCreateRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String bookID;
    private String series;
    private String description;
    private String language;
    private String isbn;
    private String format;
    private String edition;
    private Integer pages;
    private String publisher;
    private Date publishDate;
    private Date firstPublishDate;
    private Integer bbeScore;
    private Integer bbeVotes;
    private BigDecimal price;


    public Book createBookEntity(BookCreateRequestDTO createRequest, PublisherRepository publisherRepository, SeriesRepository seriesRepository) {
        Book book = new Book();
        book.setBookID(createRequest.getBookID());
        book.setTitle(createRequest.getTitle());
        book.setIsbn(createRequest.getIsbn());

        if (createRequest.getLanguage() != null) {
            book.setLanguage(Language.fromString(createRequest.getLanguage()));
        }

        if (createRequest.getFormat() != null) {
            book.setFormat(Format.fromString(createRequest.getFormat()));
        }

        book.setPages(createRequest.getPages());
        book.setPrice(createRequest.getPrice());
        book.setBbeScore(createRequest.getBbeScore());
        book.setBbeVotes(createRequest.getBbeVotes());
        book.setPublishDate(createRequest.getPublishDate());
        book.setFirstPublishDate(createRequest.getFirstPublishDate());

        if (createRequest.getPublisher() != null) {
            book.setPublisher(
                    publisherRepository.findByName(createRequest.getPublisher())
                            .orElseGet(() -> publisherRepository.save(new Publisher(createRequest.getPublisher())))
            );
        }

        if (createRequest.getSeries() != null) {
            book.setSeries(
                    seriesRepository.findByTitle(createRequest.getSeries())
                            .orElseGet(() -> seriesRepository.save(new Series(createRequest.getSeries())))
            );
        }

        return book;
    }
}
