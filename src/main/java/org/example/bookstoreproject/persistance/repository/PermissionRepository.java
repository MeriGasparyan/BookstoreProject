package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.PermissionName;
import org.example.bookstoreproject.persistance.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(PermissionName name);
   @Query("""
           select p from Permission p
           where p.name in (:permissionSet)""")
    Set<Permission> findPermissions(Set<PermissionName> permissionSet);
}
