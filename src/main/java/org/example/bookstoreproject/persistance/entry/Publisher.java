package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "publisher")
@Setter
@Getter
@NoArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publisher_id_seq")
    @SequenceGenerator(
            name = "publisher_id_seq",
            sequenceName = "publisher_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Publisher(String name) {
        this.name = name;
    }
}
