package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
}
