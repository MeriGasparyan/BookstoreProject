package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookGenre;
import org.example.bookstoreproject.persistance.entity.Genre;
import org.example.bookstoreproject.persistance.repository.BookGenreRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.GenreRepository;
import org.example.bookstoreproject.service.dto.CreateGenreDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final BookGenreRepository bookGenreRepository;

    @Transactional
    public Genre createGenre(CreateGenreDTO genreDTO) {
        String name = genreDTO.getName();
        if (genreRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Genre with name '" + name + "' already exists");
        }
        Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Transactional
    public Book addGenresToBook(Long bookId, List<Long> genreIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        List<Genre> genres = findGenreById(genreIds);

        genres.forEach(genre -> {
            if (bookGenreRepository.findByBookAndGenre(book, genre).isEmpty()) {
                book.addBookGenre(new BookGenre(book, genre));
            }
        });
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeGenresFromBook(Long bookId, List<Long> genreIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found"));

        List<Genre> genres = findGenreById(genreIds);

        genres.forEach(genre -> bookGenreRepository.findByBookAndGenre(book, genre)
                .ifPresentOrElse(
                        bookGenre -> {
                            book.getBookGenres().remove(bookGenre);
                            bookGenreRepository.delete(bookGenre);
                            bookGenre.setBook(null);
                            bookGenre.setGenre(null);
                        },
                        () -> {
                            throw new IllegalStateException(
                                    "Genre with ID " + genre.getId() +
                                            " is not associated with book " + bookId);
                        }
                ));

        return bookRepository.save(book);
    }

    private List<Genre> findGenreById(List<Long> genreIds) {
        List<Genre> genres = genreRepository.findAllById(genreIds);
        if (genres.size() != genreIds.size()) {
            Set<Long> foundGenreIds = genres.stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = genreIds.stream()
                    .filter(id -> !foundGenreIds.contains(id))
                    .toList();

            throw new NoSuchElementException("Genre(s) with ID(s) " + missingIds + " not found");
        }
        return genres;
    }
}