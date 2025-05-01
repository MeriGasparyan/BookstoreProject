package org.example.bookstoreproject.persistance.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_award")
@Setter
@Getter
@NoArgsConstructor
public class BookAward {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_award_id_seq")
    @SequenceGenerator(
            name = "book_award_id_seq",
            sequenceName = "book_award_id_seq",
            allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "award_id")
    private Award award;

    public BookAward(Book book, Award award) {
        this.book = book;
        this.award = award;
    }
}
