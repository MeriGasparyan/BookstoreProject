package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.UserRoleName;
import org.example.bookstoreproject.persistance.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleEntityRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findByName(UserRoleName userRoleName);;

}
