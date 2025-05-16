package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.OrderItem;
import org.example.bookstoreproject.service.dto.MostBoughtBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
           SELECT new org.example.bookstoreproject.service.dto.MostBoughtBookDTO(
               oi.book.id, oi.book.title, COUNT(oi.id)
           )
           FROM OrderItem oi
           GROUP BY oi.book.id, oi.book.title
           ORDER BY COUNT(oi.id) DESC
           """)
    Page<MostBoughtBookDTO> findMostBoughtBooks(Pageable pageable);
}
