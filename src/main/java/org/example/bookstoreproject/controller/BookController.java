package org.example.bookstoreproject.controller;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.service.dto.BookCreateRequestDTO;
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
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/book")
public class BookController {
    private BookService bookService;
    private RatingService ratingService;

    @GetMapping("/title/{title}")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable String title) {
        BookDTO bookDTO = bookService.getBookByTitle(title);
        if (bookDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(bookDTO);
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

    @PostMapping("/rate/{bookId}/{star}")
    public ResponseEntity<String> rateBook(@PathVariable Long bookId, @PathVariable Integer star) {
        if (star < 1 || star > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rating star must be between 1 and 5.");
        }
        try {
            ratingService.rateBook(bookId, star);
            return ResponseEntity.ok("Book rated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rating book: " + e.getMessage());
        }
    }

}
