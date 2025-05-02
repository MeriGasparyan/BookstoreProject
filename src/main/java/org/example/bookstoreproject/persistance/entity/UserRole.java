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
@Table(name = "user_role")
public class UserRole {

    @Id
    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleName name;

    public UserRole(UserRoleName name) {
        this.name = name;
    }
}