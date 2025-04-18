package org.example.bookstoreproject.persistance.entry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;

import java.util.*;

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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookAward> bookAwards = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookCharacter> bookCharacters = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookGenre> bookGenres = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookSetting> bookSettings = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookRatingStar> BookRatingStars = new ArrayList<>();

    public void addBookAuthor(BookAuthor bookAuthor) {
        bookAuthors.add(bookAuthor);
        bookAuthor.setBook(this);
    }

    public void addBookAward(BookAward bookAward) {
        bookAwards.add(bookAward);
        bookAward.setBook(this);
    }

    public void addBookCharacter(BookCharacter bookCharacter) {
        bookCharacters.add(bookCharacter);
        bookCharacter.setBook(this);
    }
    public void addBookGenre(BookGenre bookGenre) {
        bookGenres.add(bookGenre);
        bookGenre.setBook(this);
    }
    public void addBookSetting(BookSetting bookSetting) {
        bookSettings.add(bookSetting);
        bookSetting.setBook(this);
    }

    public void addBookRatingStar(BookRatingStar bookRatingStar) {
        BookRatingStars.add(bookRatingStar);
        bookRatingStar.setBook(this);
    }

    public void clearBookAuthors() {
        if (bookAuthors != null) {
            bookAuthors.forEach(bookAuthor -> bookAuthor.setBook(null));
            bookAuthors.clear();
        }
    }

    public void clearBookAwards() {
        if (bookAwards != null) {
            bookAwards.forEach(bookAward -> bookAward.setBook(null));
            bookAwards.clear();
        }
    }

    public void clearBookCharacters() {
        if (bookCharacters != null) {
            bookCharacters.forEach(bookCharacter -> bookCharacter.setBook(null));
            bookCharacters.clear();
        }
    }

    public void clearBookGenres() {
        if (bookGenres != null) {
            bookGenres.forEach(bookGenre -> bookGenre.setBook(null));
            bookGenres.clear();
        }
    }

    public void clearBookSettings() {
        if (bookSettings != null) {
            bookSettings.forEach(bookSetting -> bookSetting.setBook(null));
            bookSettings.clear();
        }
    }

}
