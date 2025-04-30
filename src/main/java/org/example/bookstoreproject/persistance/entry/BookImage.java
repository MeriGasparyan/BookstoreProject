package org.example.bookstoreproject.persistance.entry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.ImageSize;

@Entity
@Table(name = "book_image",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"size","book_id","image_id"})
})
@Setter
@Getter
@NoArgsConstructor
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_image_id_seq")
    @SequenceGenerator(
            name = "book_image_id_seq",
            sequenceName = "book_image_id_seq",
            allocationSize = 50)
    private Long id;

    @Column(nullable = false, name = "size")
    @Enumerated(EnumType.STRING)
    private ImageSize imageSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private FileMetaData image;


}
