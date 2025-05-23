package org.example.bookstoreproject.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.bookstoreproject.persistance.entity.Publisher;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
}
