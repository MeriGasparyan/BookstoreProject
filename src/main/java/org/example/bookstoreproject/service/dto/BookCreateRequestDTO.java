package org.example.bookstoreproject.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.Publisher;
import org.example.bookstoreproject.persistance.entry.Series;
import org.example.bookstoreproject.persistance.repository.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BookCreateRequestDTO {
    private String title;
    private String bookID;
    private String series;
    private List<String> author;
    private String description;
    private String language;
    private String isbn;
    private List<String> genres;
    private List<String> characters;
    private String format;
    private String edition;
    private Integer pages;
    private String publisher;
    private Date publishDate;
    private Date firstPublishDate;
    private List<String> awards;
    private List<String> settings;
    private Integer bbeScore;
    private Integer bbeVotes;
    private Float price;


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
