package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(
        name = "book",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"bookID", "title"})
        }
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String bookID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language")
    private LanguageEntity language;

    @Column(nullable = false)
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "format")
    private FormatEntity format;

    @Column
    private Integer pages;

    @Column
    private Float price;

    @Column(name = "Publish date", nullable = false)
    private Date publishDate;

    @Column(name = "First publish date", nullable = false)
    private Date firstPublishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    public Book(String title, String bookID, LanguageEntity language, String isbn, FormatEntity format, Integer pages, Float price, Date publishDate, Date firstPublishDate, Publisher publisher, Series series) {
        this.title = title;
        this.bookID = bookID;
        this.language = language;
        this.isbn = isbn;
        this.format = format;
        this.pages = pages;
        this.price = price;
        this.publishDate = publishDate;
        this.firstPublishDate = firstPublishDate;
        this.publisher = publisher;
        this.series = series;


    }
}
