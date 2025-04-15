package org.example.bookstoreproject.persistance.entry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
                @UniqueConstraint(columnNames = {"book_id", "title"})
        }
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    @SequenceGenerator(
            name = "book_id_seq",
            sequenceName = "book_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "book_id")
    private String bookID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language", referencedColumnName = "language")
    private LanguageEntity language;

    @Column
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "format",referencedColumnName = "format")
    private FormatEntity format;

    @Column
    private Integer pages;

    @Column
    private Float price;

    @Column(name = "publish_date")
    private Date publishDate;

    @Column(name = "first_publish_date")
    private Date firstPublishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Series series;

    @Column(name = "bbe_score")
    private Integer bbeScore;

    @Column(name = "bbe_votes")
    private Integer bbeVotes;


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
