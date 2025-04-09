package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rating",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rating", "book_id"})}
)
@Setter
@Getter
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float rating;

    @Column
    private Integer bbeScore;

    @Column
    private Integer bbeVotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public Rating(Float rating, Integer bbeScore, Integer bbeVotes, Book book) {
        this.rating = rating;
        this.bbeScore = bbeScore;
        this.bbeVotes = bbeVotes;
        this.book = book;
    }
}
