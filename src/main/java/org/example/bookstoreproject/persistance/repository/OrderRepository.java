package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.OrderStatus;
import org.example.bookstoreproject.persistance.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long id);

    @Query("""
            select o from Order o
            where o.user.id = :id
            """)
    List<Order> findByUserId(Long id);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByPaymentId(Long paymentId);


}
