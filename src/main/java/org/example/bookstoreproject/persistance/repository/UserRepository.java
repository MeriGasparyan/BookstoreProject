package org.example.bookstoreproject.persistance.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.bookstoreproject.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(@NotBlank @Email String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE u.email = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
}
