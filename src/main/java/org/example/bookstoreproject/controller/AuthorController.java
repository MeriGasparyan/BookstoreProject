package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.service.criteria.AuthorSearchCriteria;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.example.bookstoreproject.service.dto.CreateAuthorDTO;
import org.example.bookstoreproject.service.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody CreateAuthorDTO authorDTO) {
        try {
            Author author = authorService.createAuthor(authorDTO);
            return new ResponseEntity<>(AuthorDTO.fromEntity(author), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<List<AuthorDTO>> searchAuthors(@ModelAttribute AuthorSearchCriteria criteria) {
        List<Author> authors = authorService.getAuthors(criteria);
        List<AuthorDTO> dtos = authors.stream()
                .map(AuthorDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

}
