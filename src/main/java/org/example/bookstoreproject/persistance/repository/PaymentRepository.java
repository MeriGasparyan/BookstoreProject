package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
