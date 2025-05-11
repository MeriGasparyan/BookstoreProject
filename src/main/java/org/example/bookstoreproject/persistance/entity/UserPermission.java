package org.example.bookstoreproject.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_permission")
@Setter
@Getter
@NoArgsConstructor
public class UserPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_permission_id_seq")
    @SequenceGenerator(
            name = "user_permission_id_seq",
            sequenceName = "user_permission_id_seq",
            allocationSize = 50)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
