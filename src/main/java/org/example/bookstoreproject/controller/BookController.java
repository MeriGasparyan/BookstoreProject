package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.persistance.entity.UserBookRating;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
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
import org.springframework.security.access.AccessDeniedException;
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
    private final PriceService priceService;
    private final PermissionService permissionService;


    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id,
                                              @RequestBody BookUpdateRequestDTO request,
                                              @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "EDIT_BOOK");
        Book updated = bookService.updateBook(id, request);
        return ResponseEntity.ok(BookDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "DELETE_BOOK");
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/authors")
    public ResponseEntity<BookDTO> addBookAuthor(@PathVariable Long id,
                                                 @RequestBody @Valid BookAuthorCreateDTO request,
                                                 @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = authorService.addAuthorsToBook(id, request.getAuthors());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @DeleteMapping("/{id}/authors")
    public ResponseEntity<BookDTO> deleteBookAuthor(@PathVariable Long id,
                                                    @RequestBody BookAuthorCreateDTO request,
                                                    @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = authorService.removeAuthorsFromBook(id, request.getAuthors());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @PostMapping("/{id}/awards")
    public ResponseEntity<BookDTO> addBookAward(@PathVariable Long id,
                                                @RequestBody @Valid BookAwardCreateDTO request,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = awardService.addAwardsToBook(id, request.getAwards());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @DeleteMapping("/{id}/awards")
    public ResponseEntity<BookDTO> deleteBookAward(@PathVariable Long id,
                                                   @RequestBody BookAwardCreateDTO request,
                                                   @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = awardService.removeAwardsFromBook(id, request.getAwards());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @PostMapping("/{id}/characters")
    public ResponseEntity<BookDTO> addBookCharacter(@PathVariable Long id,
                                                    @RequestBody @Valid BookCharacterCreateDTO request,
                                                    @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = characterService.addCharactersToBook(id, request.getCharacters());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @DeleteMapping("/{id}/characters")
    public ResponseEntity<BookDTO> deleteBookCharacter(@PathVariable Long id,
                                                       @RequestBody BookCharacterCreateDTO request,
                                                       @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = characterService.removeCharactersFromBook(id, request.getCharacters());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @PostMapping("/{id}/genres")
    public ResponseEntity<BookDTO> addBookGenre(@PathVariable Long id,
                                                @RequestBody @Valid BookGenreCreateDTO request,
                                                @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = genreService.addGenresToBook(id, request.getGenres());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @DeleteMapping("/{id}/genres")
    public ResponseEntity<BookDTO> deleteBookGenre(@PathVariable Long id,
                                                   @RequestBody BookGenreCreateDTO request,
                                                   @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = genreService.removeGenresFromBook(id, request.getGenres());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @PostMapping("/{id}/settings")
    public ResponseEntity<BookDTO> addBookSetting(@PathVariable Long id,
                                                  @RequestBody @Valid BookSettingCreateDTO request,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = settingService.addSettingsToBook(id, request.getSettings());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @DeleteMapping("/{id}/settings")
    public ResponseEntity<BookDTO> deleteBookSetting(@PathVariable Long id,
                                                     @RequestBody BookSettingCreateDTO request,
                                                     @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        Book book = settingService.removeSettingsFromBook(id, request.getSettings());
        return ResponseEntity.ok(BookDTO.fromEntity(book));
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody BookCreateRequestDTO createRequest,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "ADD_BOOK");
        BookDTO book = bookService.addBook(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rateBook(@PathVariable Long id,
                                      @AuthenticationPrincipal CustomUserDetails user,
                                      @Valid @RequestBody RatingDTO request) {
        permissionService.checkPermission(user, "RATE_BOOKS");
        UserBookRating savedRating = ratingService.rateBook(user.getId(), id, request);
        return ResponseEntity.ok(RatingResponseDTO.fromEntity(savedRating));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("id") Long bookId,
                                                        @RequestParam(value = "size", defaultValue = "original") String size,
                                                        @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "VIEW_BOOKS");
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
    public ResponseEntity<PageResponseDto<BookDTO>> recommendBooksByGenre(@PathVariable Long bookId,
                                                                          @ModelAttribute BookSearchCriteria criteria,
                                                                          @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "VIEW_BOOKS");
        Page<BookDTO> recommended = recommendationService.recommendBooksByGenres(bookId, criteria.toPageable());
        return ResponseEntity.ok(PageResponseDto.from(recommended));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<PageResponseDto<RatingResponseDTO>> getBookReviews(@PathVariable Long id,
                                                                             @ModelAttribute BookSearchCriteria criteria,
                                                                             @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "VIEW_REVIEWS");
        Page<RatingResponseDTO> reviews = ratingService.getReviewsByBookId(id, criteria.toPageable());
        return ResponseEntity.ok(PageResponseDto.from(reviews));
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<BookDTO> updateBookPrice(@PathVariable Long id,
                                                   @Valid @RequestBody BookPriceUpdateDTO priceUpdateDTO,
                                                   @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_PRICING");
        permissionService.checkPermission(user, "MANAGE_DISCOUNTS");
        BookDTO updatedBook = priceService.setPriceInformation(id, priceUpdateDTO);
        return ResponseEntity.ok(updatedBook);
    }
}
