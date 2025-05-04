package org.example.bookstoreproject.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.UserRoleName;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_role_entity")
public class UserRoleEntity {

    @Id
    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleName name;

    public UserRoleEntity(UserRoleName name) {
        this.name = name;
    }
}