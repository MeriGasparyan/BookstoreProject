package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;

import java.text.DateFormat;

@Entity
@Table(name = "book")
@Setter
@Getter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private Language language;

    @Column(nullable = false)
    private String isbn;

    @Column
    private Format format;

    @Column
    private Integer pages;

    @Column
    private Float price;

    @Column(name = "Publish date", nullable = false)
    private DateFormat publishDate;

    @Column(name = "First publish date", nullable = false)
    private DateFormat firstPublishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;
}
