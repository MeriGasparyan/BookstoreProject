package org.example.bookstoreproject.persistance.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_character")
@Setter
@Getter
@NoArgsConstructor
public class BookCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_character_id_seq")
    @SequenceGenerator(
            name = "book_character_id_seq",
            sequenceName = "book_character_id_seq",
            allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    public BookCharacter(Book book, Character character) {
        this.book = book;
        this.character = character;
    }
}
