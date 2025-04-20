package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.dto.BookCreateRequestDTO;
import org.example.bookstoreproject.service.dto.BookUpdateRequestDTO;
import org.example.bookstoreproject.service.services.AuthorService;
import org.example.bookstoreproject.service.services.AwardService;
import org.example.bookstoreproject.service.services.BookService;
import org.example.bookstoreproject.service.services.RatingService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.example.bookstoreproject.service.dto.BookDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final RatingService ratingService;
    private final AuthorService authorService;
    private final AwardService awardService;

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookUpdateRequestDTO request) {
        Book updated = bookService.updateBook(id, request);
        return new ResponseEntity<>(BookDTO.fromEntity(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/authors")
    public ResponseEntity<BookDTO> addBookAuthor(@PathVariable Long id, @RequestBody List<Long> authorIds) {
        try{
        Book book = authorService.addAuthorsToBook(id, authorIds);
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/authors")
    public ResponseEntity<BookDTO> deleteBookAuthor(@PathVariable Long id, @RequestBody List<Long> authorIds) {
        Book book = authorService.removeAuthorsFromBook(id, authorIds);
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
    }

    @PutMapping("/{id}/awards")
    public ResponseEntity<BookDTO> addBookAward(@PathVariable Long id, @RequestBody List<Long> awardIds) {
        try {
            Book book = awardService.addAwardsToBook(id, awardIds);
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/awards")
    public ResponseEntity<BookDTO> deleteBookAward(@PathVariable Long id, @RequestBody List<Long> awardIds) {
        Book book = awardService.removeAwardsFromBook(id, awardIds);
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> searchBooks(
            @ModelAttribute BookSearchCriteria criteria,
            @PageableDefault(size = 20, page = 0) Pageable pageable
    ) {
        List<BookDTO> result = bookService.searchBooks(criteria, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody BookCreateRequestDTO createRequest) {
        try {
            bookService.addBook(createRequest);
            return new ResponseEntity<>("Book added successfully.", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<String> rateBook(
            @PathVariable Long id,
            @RequestParam("star") Integer starValue) {

        if (starValue < 1 || starValue > 5) {
            return ResponseEntity.badRequest().body("Rating star must be between 1 and 5.");
        }

        try {
            ratingService.rateBook(id, starValue);
            return ResponseEntity.ok("Book rated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rating book: " + e.getMessage());
        }
    }

}
