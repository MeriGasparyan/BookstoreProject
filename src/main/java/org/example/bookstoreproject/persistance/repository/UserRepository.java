package org.example.bookstoreproject.persistance.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.bookstoreproject.persistance.entity.Author;
import org.example.bookstoreproject.persistance.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(@NotBlank @Email String email);
    @Query("select u.email from User u")
    Set<String> findAllUsernames();

    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.role r
        LEFT JOIN FETCH r.rolePermissions rp
        LEFT JOIN FETCH rp.permission
        WHERE u.email = :email
    """)
    Optional<User> findUserWithRoleAndPermissionsByEmail(@Param("email") String email);

    @Query("""
            SELECT DISTINCT u FROM User u
            """)
    List<User> findAllUsers(Pageable pageable);
}
