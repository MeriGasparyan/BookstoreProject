package org.example.bookstoreproject.persistance.entity;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_role_id_seq")
    @SequenceGenerator(
            name = "author_role_id_seq",
            sequenceName = "author_role_id_seq",
            allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role")
    private RoleEntity role;

    public AuthorRole(Author author, RoleEntity role) {
        this.author = author;
        this.role = role;
    }

}
