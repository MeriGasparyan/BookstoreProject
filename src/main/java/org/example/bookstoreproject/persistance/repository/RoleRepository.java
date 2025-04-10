package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(String roleName);
    List<RoleEntity> findAll();
}

