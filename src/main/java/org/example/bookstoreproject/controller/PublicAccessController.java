package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Author;
import org.example.bookstoreproject.persistance.repository.BookAuthorRepository;
import org.example.bookstoreproject.service.criteria.AuthorSearchCriteria;
import org.example.bookstoreproject.service.dto.*;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.services.AuthorService;
import org.example.bookstoreproject.service.services.BookService;
import org.example.bookstoreproject.service.services.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicAccessController {

    private final UserService userService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final BookAuthorRepository bookAuthorRepository;

    @PostMapping("/register")
    public ResponseEntity<CreateUserReturnDTO> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userRegistrationDto));
    }

    @GetMapping("/books")
    public ResponseEntity<PageResponseDto<BookDTO>> searchBooks(@ModelAttribute BookSearchCriteria criteria) {
        Pageable pageable = criteria.toPageable();
        PageResponseDto<BookDTO> result = bookService.searchBooks(criteria, pageable);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/authors")
    public ResponseEntity<List<AuthorInformationDTO>> searchAuthors(@ModelAttribute AuthorSearchCriteria criteria) {
        List<Author> authors = authorService.getAuthors(criteria, criteria.toPageable());
        List<AuthorInformationDTO> dtos = authors.stream()
                .map(a -> AuthorInformationDTO.fromEntity(a, bookAuthorRepository))
                .toList();
        return ResponseEntity.ok(dtos);
    }
}

