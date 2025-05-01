package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookSetting;
import org.example.bookstoreproject.persistance.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookSettingRepository extends JpaRepository<BookSetting, Long> {
boolean existsByBookAndSetting(Book book, Setting setting);
List<BookSetting> findByBook(Book book);

    Optional<BookSetting> findByBookAndSetting(Book book, Setting setting);
}
