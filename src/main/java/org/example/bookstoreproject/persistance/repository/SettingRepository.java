package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
}
