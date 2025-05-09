package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.PermissionName;
import org.example.bookstoreproject.persistance.entity.Permission;
import org.example.bookstoreproject.persistance.entity.RolePermission;
import org.example.bookstoreproject.persistance.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    boolean existsByRoleAndPermission(UserRole role, Permission permission);
    Optional<RolePermission> findByRoleAndPermission(UserRole role, Permission permission);
    Optional<RolePermission> findByRole(UserRole role);

    @Query("""
select p from RolePermission p
where p.permission.name = :permission
""")
    Optional<RolePermission> findByPermissionName(PermissionName permission);
}
