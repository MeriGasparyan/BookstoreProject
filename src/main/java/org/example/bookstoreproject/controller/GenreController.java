package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.service.dto.CreateGenreDTO;
import org.example.bookstoreproject.service.dto.GenreDTO;
import org.example.bookstoreproject.service.services.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;
    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@RequestBody CreateGenreDTO genreDTO) {
        try {
            Genre genre = genreService.createGenre(genreDTO);
            return new ResponseEntity<>(GenreDTO.fromEntity(genre), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
