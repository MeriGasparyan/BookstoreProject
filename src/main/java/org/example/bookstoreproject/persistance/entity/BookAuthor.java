package org.example.bookstoreproject.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_author")
@Setter
@Getter
@NoArgsConstructor
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_author_id_seq")
    @SequenceGenerator(
            name = "book_author_id_seq",
            sequenceName = "book_author_id_seq",
            allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    public BookAuthor(Book book, Author author) {
        this.book = book;
        this.author = author;
    }
}
