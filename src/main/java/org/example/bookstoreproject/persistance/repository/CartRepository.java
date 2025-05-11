package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findById(Long id);
    Optional<Cart> findByUserId(Long userId);
}
