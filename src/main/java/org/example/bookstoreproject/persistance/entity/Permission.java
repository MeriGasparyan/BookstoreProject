package org.example.bookstoreproject.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.enums.PermissionName;

@Getter
@Setter
@Entity
@Table(name = "permissions")
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionName name;
}
