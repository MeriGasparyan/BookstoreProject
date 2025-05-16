package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Author;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.AuthorDTO;
import org.example.bookstoreproject.service.dto.CreateAuthorDTO;
import org.example.bookstoreproject.service.services.AuthorService;
import org.example.bookstoreproject.service.services.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody @Valid CreateAuthorDTO authorDTO,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
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
