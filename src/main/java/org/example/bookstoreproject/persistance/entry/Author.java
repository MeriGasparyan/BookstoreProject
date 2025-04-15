package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "author")
@Setter
@Getter
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    @SequenceGenerator(
            name = "author_id_seq",
            sequenceName = "author_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Author(String name) {
        this.name = name;
    }

}
