package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.PermissionName;
import org.example.bookstoreproject.persistance.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(PermissionName name);
}
