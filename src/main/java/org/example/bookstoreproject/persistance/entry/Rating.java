package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rating",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rating", "bookID"})}
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

    @Column(nullable = false)
    private String bookID;

    @Column
    private Integer bbeScore;

    @Column
    private Integer bbeVotes;

    public Rating(Float rating, String bookID, Integer bbeScore, Integer bbeVotes) {
        this.rating = rating;
        this.bookID = bookID;
        this.bbeScore = bbeScore;
        this.bbeVotes = bbeVotes;
    }
}
