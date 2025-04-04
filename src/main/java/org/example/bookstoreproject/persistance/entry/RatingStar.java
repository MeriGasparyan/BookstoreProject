package org.example.bookstoreproject.persistance.entry;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "star_rating",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"rating", "star"})
        }
)
@Setter
@Getter
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

    @Column(nullable = false)
    private Long numRating;
}
