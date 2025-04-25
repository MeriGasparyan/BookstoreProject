package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.dto.*;
import org.example.bookstoreproject.service.services.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final RatingService ratingService;
    private final AuthorService authorService;
    private final AwardService awardService;
    private final GenreService genreService;
    private final CharacterService characterService;
    private final SettingService settingService;
    private final ImageDataService metadataService;

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

    @PostMapping("/{id}/authors")
    public ResponseEntity<BookDTO> addBookAuthor(@PathVariable Long id, @RequestBody BookAuthorUpdateDTO request) {
        try{
        Book book = authorService.addAuthorsToBook(id, request.getAuthors());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/authors")
    public ResponseEntity<BookDTO> deleteBookAuthor(@PathVariable Long id, @RequestBody BookAuthorUpdateDTO request) {
        Book book = authorService.removeAuthorsFromBook(id, request.getAuthors());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/awards")
    public ResponseEntity<BookDTO> addBookAward(@PathVariable Long id, @RequestBody BookAwardUpdateDTO request) {
        try {
            Book book = awardService.addAwardsToBook(id, request.getAwards());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/awards")
    public ResponseEntity<BookDTO> deleteBookAward(@PathVariable Long id, @RequestBody BookAwardUpdateDTO request) {
        Book book = awardService.removeAwardsFromBook(id, request.getAwards());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/characters")
    public ResponseEntity<BookDTO> addBookCharacter(@PathVariable Long id, @RequestBody BookCharacterUpdateDTO request) {
        try {
            Book book = characterService.addCharactersToBook(id, request.getCharacters());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/characters")
    public ResponseEntity<BookDTO> deleteBookCharacter(@PathVariable Long id, @RequestBody BookCharacterUpdateDTO request) {
        Book book = characterService.removeCharactersFromBook(id, request.getCharacters());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/genres")
    public ResponseEntity<BookDTO> addBookGenre(@PathVariable Long id, @RequestBody BookGenreUpdateDTO request) {
        try {
            Book book = genreService.addGenresToBook(id, request.getGenres());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/genres")
    public ResponseEntity<BookDTO> deleteBookGenre(@PathVariable Long id, @RequestBody BookGenreUpdateDTO request) {
        Book book = genreService.removeGenresFromBook(id, request.getGenres());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @PostMapping ("/{id}/settings")
    public ResponseEntity<BookDTO> addBookSetting(@PathVariable Long id, @RequestBody BookSettingUpdateDTO request) {
        try {
            Book book = settingService.addSettingsToBook(id, request.getSettings());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/settings")
    public ResponseEntity<BookDTO> deleteBookSetting(@PathVariable Long id, @RequestBody BookSettingUpdateDTO request) {
        Book book = settingService.removeSettingsFromBook(id, request.getSettings());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<BookDTO>> searchBooks(
            @ModelAttribute BookSearchCriteria criteria
    ) {
        Pageable pageable = criteria.toPageable();
        PageResponseDto<BookDTO> result = bookService.searchBooks(criteria, pageable);
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
    public ResponseEntity<BookDTO> rateBook(
            @PathVariable Long id,
            @RequestBody RatingDTO ratingDTO) {
        Integer starValue = ratingDTO.getStar();
        if (starValue < 1 || starValue > 5) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Book book = ratingService.rateBook(id, starValue);
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<InputStreamResource> getImage(
            @PathVariable("id") Long bookId,
            @RequestParam(value = "size", defaultValue = "original") String size
    ) {
        String imagePath = metadataService.getImagePath(bookId, ImageSize.fromString(size));
        if (imagePath == null) {
            System.out.println(11111111);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.out.println(22222);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            InputStream inputStream = new FileInputStream(imageFile);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
