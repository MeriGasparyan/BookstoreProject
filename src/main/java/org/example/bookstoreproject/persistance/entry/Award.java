package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "award")
@Setter
@Getter
@NoArgsConstructor
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "award_id_seq")
    @SequenceGenerator(
            name = "award_id_seq",
            sequenceName = "award_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true, length = 1000)
    private String title;

    public Award(String title) {
        this.title = title;
    }
}
