package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(String roleName);
}

