package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Character;
import org.example.bookstoreproject.persistance.entity.User;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.CharacterDTO;
import org.example.bookstoreproject.service.dto.CreateCharacterDTO;
import org.example.bookstoreproject.service.services.CharacterService;
import org.example.bookstoreproject.service.services.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/characters")
public class CharacterController {
    private final CharacterService characterService;
    private final PermissionService permissionService;
    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<CharacterDTO> createCharacter(@RequestBody CreateCharacterDTO characterDTO,
                                                        @AuthenticationPrincipal CustomUserDetails user) {

        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");try {
            Character character = characterService.createCharacter(characterDTO);
            return new ResponseEntity<>(CharacterDTO.fromEntity(character), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
