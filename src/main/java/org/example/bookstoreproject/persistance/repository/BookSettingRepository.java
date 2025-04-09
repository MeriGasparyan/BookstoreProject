package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BookSettingRepository extends JpaRepository<BookSetting, Long> {
boolean existsByBookAndSetting(Book book, BookSetting setting);

}
