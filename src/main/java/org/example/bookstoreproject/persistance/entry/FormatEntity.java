package org.example.bookstoreproject.persistance.entry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "format")
@Setter
@Getter
@NoArgsConstructor
public class FormatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String format;

    public FormatEntity(String format) {
        this.format = format;
    }
}
