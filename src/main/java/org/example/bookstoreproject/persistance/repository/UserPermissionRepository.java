package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
}
