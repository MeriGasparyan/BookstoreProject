package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.persistance.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(UserRoleName userRoleName);;

}
