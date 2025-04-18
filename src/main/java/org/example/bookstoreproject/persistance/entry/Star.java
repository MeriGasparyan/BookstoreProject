package org.example.bookstoreproject.persistance.entry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.RatingStarNumber;

@Entity
@Table(name = "star")
@Getter
@Setter
@NoArgsConstructor
public class Star {

    @Id
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RatingStarNumber level;

    public Star(RatingStarNumber level) {
        this.level = level;
    }
}
