package org.example.bookstoreproject.persistance.entry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;

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
            allocationSize = 50)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "book_id")
    private String bookID;

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column
    private String isbn;

    @Column(name = "format")
    @Enumerated(EnumType.STRING)
    private Format format;

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

}
