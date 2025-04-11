package org.example.bookstoreproject.persistance.entry;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "book_rating_star",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"rating", "star"})
        }
)
@Setter
@Getter
@NoArgsConstructor
public class BookRatingStar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "star")
    private Star star;

    @Column
    private Long numRating;

    public BookRatingStar(Book book, Star star, Long numRating) {
        this.book = book;
        this.star = star;
        this.numRating = numRating;
    }
}
