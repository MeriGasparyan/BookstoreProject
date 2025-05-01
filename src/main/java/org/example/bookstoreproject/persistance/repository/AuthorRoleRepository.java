package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.Author;
import org.example.bookstoreproject.persistance.entity.AuthorRole;
import org.example.bookstoreproject.persistance.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRoleRepository extends JpaRepository<AuthorRole, Long> {
    boolean existsByAuthorAndRole(Author author, RoleEntity role);
}
