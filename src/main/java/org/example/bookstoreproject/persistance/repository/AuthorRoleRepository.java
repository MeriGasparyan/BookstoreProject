package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.AuthorRole;
import org.example.bookstoreproject.persistance.entry.Award;
import org.example.bookstoreproject.persistance.entry.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRoleRepository extends JpaRepository<AuthorRole, Long> {
    boolean existsByAuthorAndRole(Author author, RoleEntity role);

}
