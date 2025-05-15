package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.*;
import org.example.bookstoreproject.service.services.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final AwardService awardService;
    private final GenreService genreService;
    private final CharacterService characterService;
    private final SettingService settingService;
    private final ImageDataService metadataService;
    private final UserService userService;
    private final UserBookRatingService ratingService;
    private final RecommendationService recommendationService;

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_BOOK')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookUpdateRequestDTO request) {
        Book updated = bookService.updateBook(id, request);
        return new ResponseEntity<>(BookDTO.fromEntity(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_BOOK')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/authors")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> addBookAuthor(@PathVariable Long id, @RequestBody @Valid BookAuthorCreateDTO request) {
        try {
            Book book = authorService.addAuthorsToBook(id, request.getAuthors());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/authors")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> deleteBookAuthor(@PathVariable Long id, @RequestBody BookAuthorCreateDTO request) {
        Book book = authorService.removeAuthorsFromBook(id, request.getAuthors());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/awards")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> addBookAward(@PathVariable Long id, @RequestBody @Valid BookAwardCreateDTO request) {
        try {
            Book book = awardService.addAwardsToBook(id, request.getAwards());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/awards")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> deleteBookAward(@PathVariable Long id, @RequestBody BookAwardCreateDTO request) {
        Book book = awardService.removeAwardsFromBook(id, request.getAwards());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/characters")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> addBookCharacter(@PathVariable Long id, @RequestBody @Valid BookCharacterCreateDTO request) {
        try {
            Book book = characterService.addCharactersToBook(id, request.getCharacters());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/characters")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> deleteBookCharacter(@PathVariable Long id, @RequestBody BookCharacterCreateDTO request) {
        Book book = characterService.removeCharactersFromBook(id, request.getCharacters());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/genres")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> addBookGenre(@PathVariable Long id, @RequestBody @Valid BookGenreCreateDTO request) {
        try {
            Book book = genreService.addGenresToBook(id, request.getGenres());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/genres")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> deleteBookGenre(@PathVariable Long id, @RequestBody BookGenreCreateDTO request) {
        Book book = genreService.removeGenresFromBook(id, request.getGenres());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/settings")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> addBookSetting(@PathVariable Long id, @RequestBody @Valid BookSettingCreateDTO request) {
        try {
            Book book = settingService.addSettingsToBook(id, request.getSettings());
            return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/settings")
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<BookDTO> deleteBookSetting(@PathVariable Long id, @RequestBody BookSettingCreateDTO request) {
        Book book = settingService.removeSettingsFromBook(id, request.getSettings());
        return new ResponseEntity<>(BookDTO.fromEntity(book), HttpStatus.NO_CONTENT);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADD_BOOK')")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookCreateRequestDTO createRequest) {
        try {
            BookDTO book = bookService.addBook(createRequest);
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/rate")
    @PreAuthorize("hasAuthority('RATE_BOOKS')")
    public ResponseEntity<?> rateBook(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody RatingDTO request) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        User user = userService.getUserById(userDetails.getId());
        UserBookRating savedRating = ratingService.rateBook(user, id, request);
        RatingResponseDTO response = RatingResponseDTO.fromEntity(savedRating);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<InputStreamResource> getImage(
            @PathVariable("id") Long bookId,
            @RequestParam(value = "size", defaultValue = "original") String size
    ) {

        try {
            InputStreamResource inputStreamResource = metadataService.getImage(bookId, ImageSize.fromString(size));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{bookId}/recommend")
    public ResponseEntity<PageResponseDto<BookDTO>> recommendBooksByGenre(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookDTO> recommended = recommendationService.recommendBooksByGenres(bookId, pageable);

        return ResponseEntity.ok(PageResponseDto.from(recommended));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<PageResponseDto<RatingResponseDTO>> getBookReviews(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RatingResponseDTO> reviews = ratingService.getReviewsByBookId(id, pageable);
        return ResponseEntity.ok(PageResponseDto.from(reviews));
    }


}
