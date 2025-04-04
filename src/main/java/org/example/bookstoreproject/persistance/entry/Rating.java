package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rating")
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
}
