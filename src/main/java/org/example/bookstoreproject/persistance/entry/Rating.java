package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "character")
@Setter
@Getter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float rating;

    @Column(nullable = false, name = "Total num of ratings")
    private Long numRatings;

    @Column(nullable = false, name = "Percentage of liked")
    private Float likedPercent;

    @Column(nullable = false)
    private Long numRating5;

    @Column(nullable = false)
    private Long numRating4;

    @Column(nullable = false)
    private Long numRating3;

    @Column(nullable = false)
    private Long numRating2;

    @Column(nullable = false)
    private Long numRating1;

    @Column
    private Integer bbeScore;

    @Column
    private Integer bbeVotes;
}
