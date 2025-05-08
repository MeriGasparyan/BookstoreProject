package org.example.bookstoreproject.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.UserRoleName;

import java.util.ArrayList;
import java.util.List;


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

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RolePermission> rolePermissions = new ArrayList<>();

    public void addRolePermission(RolePermission rolePermission) {
        rolePermissions.add(rolePermission);
        rolePermission.setRole(this);
    }

    public void removeRolePermission(RolePermission rolePermission) {
        rolePermissions.remove(rolePermission);
        rolePermission.setRole(null);
    }

    public UserRole(UserRoleName name) {
        this.name = name;
    }
}