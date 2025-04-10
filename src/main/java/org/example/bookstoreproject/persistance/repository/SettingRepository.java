package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findByName(String name);
    List<Setting> findAll();
}
