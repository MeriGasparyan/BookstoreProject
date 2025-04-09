package org.example.bookstoreproject.controller;

import org.example.bookstoreproject.service.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.example.bookstoreproject.service.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/title/{title}")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable String title) {
        BookDTO bookDTO = bookService.getBookByTitle(title);
        if (bookDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping("/author/name/{authorName}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthorName(@PathVariable String authorName) {
        List<BookDTO> books = bookService.getBooksByAuthorName(authorName);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(books);
    }


}
