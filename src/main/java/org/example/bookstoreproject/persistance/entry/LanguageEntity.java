package org.example.bookstoreproject.persistance.entry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "language")
@Setter
@Getter
@NoArgsConstructor
public class LanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String language;

    public LanguageEntity(String language) {
        this.language = language;
    }
}
