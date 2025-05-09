package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
