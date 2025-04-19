package org.example.bookstoreproject.persistance.repository;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Book;
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

    Optional<Book> findByBookIDAndTitle(String bookID, String title);
    Optional<Book> findByTitle(String title);
    Optional<Book> findByBookID(String bookID);

    @Query("SELECT b.bookID FROM Book b")
    Set<String> findAllBookIds();

    List<Book> findAllByBookIDIn(Set<String> bookIDs);

    @Query("SELECT b.bookID FROM Book b WHERE b.bookID IN :ids")
    Set<String> findBookIdsByBookIdIn(@Param("ids") List<String> ids);

    @Query("""
    SELECT DISTINCT b FROM Book b
    JOIN BookAuthor ba ON ba.book.id = b.id
    JOIN Author a ON a.id = ba.author.id
    JOIN BookGenre bg ON bg.book.id = b.id
    JOIN Genre g ON g.id = bg.genre.id
    JOIN BookAward baw ON baw.book.id = b.id
    JOIN Award aw ON aw.id = baw.award.id
    JOIN BookCharacter bc ON bc.book.id = b.id
    JOIN Character c ON c.id = bc.character.id
    JOIN BookSetting bs ON bs.book.id = b.id
    JOIN Setting s ON s.id = bs.setting.id
    JOIN Publisher p ON p.id = b.publisher.id
    JOIN Series sr ON sr.id = b.series.id
    WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) OR :title IS NULL OR b.title IS NULL)
    AND (:authorIds IS NULL OR a.id IN :authorIds)
    AND (:genreIds IS NULL OR g.id IN :genreIds)
    AND (:publisherIds IS NULL OR p.id IN :publisherIds)
    AND (:seriesIds IS NULL OR sr.id IN :seriesIds)
    AND (:awardIds IS NULL OR aw.id IN :awardIds)
    AND (:characterIds IS NULL OR c.id IN :characterIds)
    AND (:settingIds IS NULL OR s.id IN :settingIds)
    AND (:language IS NULL OR b.language = :language)
    
    GROUP BY b
    HAVING 
        (:authorIds IS NULL OR COUNT(DISTINCT a.id) = :authorIdsSize) AND
        (:genreIds IS NULL OR COUNT(DISTINCT g.id) = :genreIdsSize) AND
        (:publisherIds IS NULL OR COUNT(DISTINCT p.id) = :publisherIdsSize) AND
        (:seriesIds IS NULL OR COUNT(DISTINCT sr.id) = :seriesIdsSize) AND
        (:awardIds IS NULL OR COUNT(DISTINCT aw.id) = :awardIdsSize) AND
        (:characterIds IS NULL OR COUNT(DISTINCT c.id) = :characterIdsSize) AND
        (:settingIds IS NULL OR COUNT(DISTINCT s.id) = :settingIdsSize)
    """)
    List<Book> searchBooks(
            @Param("title") String title,
            @Param("authorIds") List<Long> authorIds,
            @Param("genreIds") List<Long> genreIds,
            @Param("publisherIds") List<Long> publisherIds,
            @Param("seriesIds") List<Long> seriesIds,
            @Param("awardIds") List<Long> awardIds,
            @Param("characterIds") List<Long> characterIds,
            @Param("settingIds") List<Long> settingIds,
            @Param("authorIdsSize") Integer authorIdsSize,
            @Param("genreIdsSize") Integer genreIdsSize,
            @Param("publisherIdsSize") Integer publisherIdsSize,
            @Param("seriesIdsSize") Integer seriesIdsSize,
            @Param("awardIdsSize") Integer awardIdsSize,
            @Param("characterIdsSize") Integer characterIdsSize,
            @Param("settingIdsSize") Integer settingIdsSize,
            @Param("language") Language language,
            Pageable pageable
    );




}
