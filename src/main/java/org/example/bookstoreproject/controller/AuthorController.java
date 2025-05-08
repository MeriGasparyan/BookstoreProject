package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Author;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.example.bookstoreproject.service.dto.CreateAuthorDTO;
import org.example.bookstoreproject.service.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody @Valid CreateAuthorDTO authorDTO) {
        try {
            Author author = authorService.createAuthor(authorDTO);
            return new ResponseEntity<>(AuthorDTO.fromEntity(author), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
