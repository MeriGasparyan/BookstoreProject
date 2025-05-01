package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Author;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookAuthor;
import org.example.bookstoreproject.persistance.repository.AuthorRepository;
import org.example.bookstoreproject.persistance.repository.BookAuthorRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.service.criteria.AuthorSearchCriteria;
import org.example.bookstoreproject.service.dto.CreateAuthorDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;

    @Transactional
    public Author createAuthor(CreateAuthorDTO authorDTO) {
        String name = authorDTO.getName();
        if (authorRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Author with name '" + name + "' already exists");
        }
        Author author = new Author(name);
        return authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Author> getAuthors(AuthorSearchCriteria authorSearchCriteria, Pageable pageable) {
        return authorRepository.searchAuthor(authorSearchCriteria.getId(), authorSearchCriteria.getName(),pageable);
    }

    @Transactional
    public Author updateAuthor(Long id, String newName) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author with ID " + id + " not found"));

        Optional<Author> existingAuthor = authorRepository.findByName(newName);
        if (existingAuthor.isPresent() && !existingAuthor.get().getId().equals(id)) {
            throw new IllegalArgumentException("Author with name '" + newName + "' already exists");
        }

        author.setName(newName);
        return authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new NoSuchElementException("Author with ID " + id + " not found");
        }
        authorRepository.deleteById(id);
    }

    @Transactional
    public Book addAuthorsToBook(Long bookId, List<Long> authorIds) {
        System.out.println(222);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        List<Author> authors = authorRepository.findAllById(authorIds);

        if (authors.size() != authorIds.size()) {
            Set<Long> foundAuthorIds = authors.stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = authorIds.stream()
                    .filter(id -> !foundAuthorIds.contains(id))
                    .toList();
            System.out.println(11111);
            throw new NoSuchElementException("Author(s) with ID(s) " + missingIds + " not found");
        }

        authors.forEach(author -> {
            if (bookAuthorRepository.findByBookAndAuthor(book, author).isEmpty()) {
                book.addBookAuthor(new BookAuthor(book, author));
            }
        });
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeAuthorsFromBook(Long bookId, List<Long> authorIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found"));

        List<Author> authors = authorRepository.findAllById(authorIds);
        if (authors.size() != authorIds.size()) {
            Set<Long> foundAuthorIds = authors.stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = authorIds.stream()
                    .filter(id -> !foundAuthorIds.contains(id))
                    .toList();

            throw new NoSuchElementException("Author(s) with ID(s) " + missingIds + " not found");
        }

        authors.forEach(author -> {
            bookAuthorRepository.findByBookAndAuthor(book, author)
                    .ifPresentOrElse(
                            bookAuthor -> {

                                book.getBookAuthors().remove(bookAuthor);


                                bookAuthorRepository.delete(bookAuthor);
                                bookAuthor.setBook(null);
                                bookAuthor.setAuthor(null);
                            },
                            () -> {
                                throw new IllegalStateException(
                                        "Author with ID " + author.getId() +
                                                " is not associated with book " + bookId);
                            }
                    );
        });

        return bookRepository.save(book);
    }
}