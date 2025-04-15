package org.example.bookstoreproject.persistance.entry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_setting")
@Setter
@Getter
@NoArgsConstructor
public class BookSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_setting_id_seq")
    @SequenceGenerator(
            name = "book_setting_id_seq",
            sequenceName = "book_setting_id_seq",
            allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private Setting setting;

    public BookSetting(Book book, Setting setting) {
        this.book = book;
        this.setting = setting;
    }
}
