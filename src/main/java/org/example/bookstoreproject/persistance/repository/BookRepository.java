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
    JOIN BookAuthor ba on ba.book.id = b.id
    JOIN Author a on a.id = ba.author.id
    JOIN BookGenre bg on bg.book.id = b.id
    JOIN Genre g on g.id = bg.genre.id
    JOIN BookAward baw on baw.book.id = b.id
    JOIN Award aw on aw.id = baw.award.id
    JOIN BookCharacter bc on bc.book.id = b.id
    JOIN Character c on c.id = bc.character.id
    JOIN BookSetting bs on bs.book.id = b.id
    JOIN Setting s on s.id = bs.setting.id
    JOIN Publisher p on p.id = b.publisher.id
    JOIN Series sr on sr.id = b.series.id
    WHERE (:title IS NULL OR b.title LIKE :title)
    AND (:authorIds IS NULL OR a.id IN :authorIds)
    AND (:genreIds IS NULL OR g.id IN :genreIds)
    AND (:publisherIds IS NULL OR p.id IN :publisherIds)
    AND (:seriesIds IS NULL OR sr.id IN :seriesIds)
    AND (:awardIds IS NULL OR aw.id IN :awardIds)
    AND (:characterIds IS NULL OR c.id IN :characterIds)
    AND (:settingIds IS NULL OR s.id IN :settingIds)
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
            Pageable pageable
    );




}
