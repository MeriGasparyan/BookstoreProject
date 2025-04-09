package org.example.bookstoreproject.persistance.entry;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "rating_star",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"rating", "star"})
        }
)
@Setter
@Getter
@NoArgsConstructor
public class RatingStar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating")
    private Rating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "star")
    private Star star;

    @Column
    private Long numRating;

    public RatingStar(Rating rating, Star star, Long numRating) {
        this.rating = rating;
        this.star = star;
        this.numRating = numRating;
    }
}
