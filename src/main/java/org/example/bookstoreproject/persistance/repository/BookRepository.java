package org.example.bookstoreproject.persistance.repository;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findByIsbnAndTitle(String isbn, String title);
    Optional<Book> findBookById(Long id);
    Optional<Book> findByBookIDAndTitle(String bookID, String title);
    Optional<Book> findByTitle(String title);
    Optional<Book> findByBookID(String bookID);

    @Query("SELECT b.bookID FROM Book b")
    Set<String> findAllBookIds();

    List<Book> findAllByBookIDIn(Set<String> bookIDs);

    @Query("SELECT b.bookID FROM Book b WHERE b.bookID IN :ids")
    Set<String> findBookIdsByBookIdIn(@Param("ids") List<String> ids);

    @Query("""
    SELECT DISTINCT new org.example.bookstoreproject.service.dto.BookDTO(b) FROM Book b
    LEFT JOIN BookAuthor ba ON ba.book.id = b.id
    LEFT JOIN Author a ON a.id = ba.author.id
    LEFT JOIN BookGenre bg ON bg.book.id = b.id
    LEFT JOIN Genre g ON g.id = bg.genre.id
    LEFT JOIN BookAward baw ON baw.book.id = b.id
    LEFT JOIN Award aw ON aw.id = baw.award.id
    LEFT JOIN BookCharacter bc ON bc.book.id = b.id
    LEFT JOIN Character c ON c.id = bc.character.id
    LEFT JOIN BookSetting bs ON bs.book.id = b.id
    LEFT JOIN Setting s ON s.id = bs.setting.id
    LEFT JOIN Publisher p ON p.id = b.publisher.id
    LEFT JOIN Series sr ON sr.id = b.series.id
    WHERE (:#{#criteria.title} IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :#{#criteria.title}, '%')))
    AND (:#{#criteria.authors} IS NULL OR a.id IN :#{#criteria.authors})
    AND (:#{#criteria.genres} IS NULL OR g.id IN :#{#criteria.genres})
    AND (:#{#criteria.publisher} IS NULL OR p.id = :#{#criteria.publisher})
    AND (:#{#criteria.series} IS NULL OR sr.id = :#{#criteria.series})
    AND (:#{#criteria.awards} IS NULL OR aw.id IN :#{#criteria.awards})
    AND (:#{#criteria.characters} IS NULL OR c.id IN :#{#criteria.characters})
    AND (:#{#criteria.settings} IS NULL OR s.id IN :#{#criteria.settings})
    AND (:#{#criteria.language} IS NULL OR b.language = :#{#criteria.language})
    GROUP BY b
    HAVING
        (:#{#criteria.authors} IS NULL OR COUNT(DISTINCT a.id) = :#{#criteria.authors == null ? 0 : #criteria.authors.size()}) AND
        (:#{#criteria.genres} IS NULL OR COUNT(DISTINCT g.id) = :#{#criteria.genres == null ? 0 : #criteria.genres.size()}) AND
        (:#{#criteria.awards} IS NULL OR COUNT(DISTINCT aw.id) = :#{#criteria.awards == null ? 0 : #criteria.awards.size()}) AND
        (:#{#criteria.characters} IS NULL OR COUNT(DISTINCT c.id) = :#{#criteria.characters == null ? 0 : #criteria.characters.size()}) AND
        (:#{#criteria.settings} IS NULL OR COUNT(DISTINCT s.id) = :#{#criteria.settings == null ? 0 : #criteria.settings.size()})
    """)
    Page<BookDTO> searchBooks(
            BookSearchCriteria criteria,
            Pageable pageable
    );




}
