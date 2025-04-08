package org.example.bookstoreproject.persistance.entry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "star")
@Getter
@Setter
@NoArgsConstructor
public class Star {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false, unique = true)
    private String level;

    public Star(String level) {
        this.level = level;
    }
}
