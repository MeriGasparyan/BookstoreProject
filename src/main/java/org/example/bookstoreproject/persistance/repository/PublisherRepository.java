package org.example.bookstoreproject.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.bookstoreproject.persistance.entry.Publisher;
import org.springframework.stereotype.Repository;
@Repository

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
