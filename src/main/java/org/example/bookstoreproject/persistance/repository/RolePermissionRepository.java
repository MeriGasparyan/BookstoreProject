package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.Permission;
import org.example.bookstoreproject.persistance.entity.RolePermission;
import org.example.bookstoreproject.persistance.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    boolean existsByRoleAndPermission(UserRole role, Permission permission);
}
