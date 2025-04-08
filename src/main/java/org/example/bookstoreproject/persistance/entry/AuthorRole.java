package org.example.bookstoreproject.persistance.entry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(
        name = "author_role",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"author", "role"})
        }
)
@NoArgsConstructor
public class AuthorRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role")
    private RoleEntity role;

}
