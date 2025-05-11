package org.example.bookstoreproject.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(
            name = "users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 50)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstname;

    @Column(name = "last_name", nullable = false)
    private String lastname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;


    @Column(nullable = false, updatable = false, name = "created_at")
    @CreationTimestamp
    private Instant createdAt;


    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserPermission> userPermissions = new ArrayList<>();

    public void addRolePermission(UserPermission userPermission) {
        userPermissions.add(userPermission);
        userPermission.setUser(this);
    }

    public void removeRolePermission(UserPermission userPermission) {
        userPermissions.remove(userPermission);
        userPermission.setUser(null);
    }


}