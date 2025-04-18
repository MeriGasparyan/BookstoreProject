package org.example.bookstoreproject.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.dto.BookCreateRequestDTO;
import org.example.bookstoreproject.service.dto.BookSearchRequestDTO;
import org.example.bookstoreproject.service.dto.BookUpdateRequestDTO;
import org.example.bookstoreproject.service.services.BookService;
import org.example.bookstoreproject.service.services.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.example.bookstoreproject.service.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final RatingService ratingService;

    @PutMapping("/{bookID}")
    public ResponseEntity<Void> updateBook(@PathVariable String bookID, @RequestBody BookUpdateRequestDTO request) {
        bookService.updateBook(bookID, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookID}")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookID) {
        bookService.deleteBook(bookID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestBody BookSearchRequestDTO request,
                                                     @RequestParam(value = "limit", defaultValue = "20") int limit) {
        List<BookDTO> result = bookService.searchBooks(request, limit)
                .stream()
                .limit(20)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
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

    @PostMapping("/{bookID}/rate")
    public ResponseEntity<String> rateBook(
            @PathVariable String bookID,
            @RequestParam("star") Integer starValue) {

        if (starValue < 1 || starValue > 5) {
            return ResponseEntity.badRequest().body("Rating star must be between 1 and 5.");
        }

        try {
            ratingService.rateBook(bookID, starValue);
            return ResponseEntity.ok("Book rated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rating book: " + e.getMessage());
        }
    }

}
