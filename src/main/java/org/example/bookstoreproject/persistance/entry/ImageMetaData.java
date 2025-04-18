package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.*;
import org.example.bookstoreproject.enums.ImageSize;


@Entity
@Table(
        name = "image_metadata",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"file_name", "book_id", "size"})
        }
)
@Setter
@Getter
@NoArgsConstructor
public class ImageMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_metadata_id_seq")
    @SequenceGenerator(
            name = "image_metadata_id_seq",
            sequenceName = "image_metadata_id_seq",
            allocationSize = 50)
    private Long id;

    @Column(nullable = false, name = "size")
    @Enumerated(EnumType.STRING)
    private ImageSize imageSize;

    @Column(nullable = false, name = "main_folder_name")
    private String mainFolderName;

    @Column(nullable = false, name = "sub_folder_name")
    private String subFolderName;

    @Column(nullable = false, name = "format")
    private String formatName;

    @Column(nullable = false, name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
