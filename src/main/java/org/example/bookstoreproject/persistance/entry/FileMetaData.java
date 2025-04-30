package org.example.bookstoreproject.persistance.entry;
import jakarta.persistence.*;

import lombok.*;


@Entity
@Table(
        name = "file_metadata",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"file_name", "main_folder_name", "sub_folder_name"})
        }
)
@Setter
@Getter
@NoArgsConstructor
public class FileMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_metadata_id_seq")
    @SequenceGenerator(
            name = "file_metadata_id_seq",
            sequenceName = "file_metadata_id_seq",
            allocationSize = 50)
    private Long id;


    @Column(nullable = false, name = "main_folder_name")
    private String mainFolderName;

    @Column(nullable = false, name = "sub_folder_name")
    private String subFolderName;

    @Column(nullable = false, name = "format")
    private String formatName;

    @Column(nullable = false, name = "file_name")
    private String fileName;
}
