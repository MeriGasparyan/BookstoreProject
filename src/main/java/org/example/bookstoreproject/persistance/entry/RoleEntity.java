package org.example.bookstoreproject.persistance.entry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Setter
@Getter
@NoArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    @SequenceGenerator(
            name = "role_id_seq",
            sequenceName = "role_id_seq",
            allocationSize = 100)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roleName;

    public RoleEntity(String roleName) {
        this.roleName = roleName;
    }
}
