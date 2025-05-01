package org.example.bookstoreproject.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "character")
@Setter
@Getter
@NoArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_id_seq")
    @SequenceGenerator(
            name = "character_id_seq",
            sequenceName = "character_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Character(String name) {
        this.name = name;
    }
}
