package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Award;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.AwardDTO;
import org.example.bookstoreproject.service.dto.CreateAwardDTO;
import org.example.bookstoreproject.service.services.AwardService;
import org.example.bookstoreproject.service.services.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/awards")
public class AwardController {
    private final AwardService awardService;
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<AwardDTO> createAward(@RequestBody @Valid CreateAwardDTO awardDTO,@AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        try {
            Award award = awardService.createAward(awardDTO);
            return new ResponseEntity<>(AwardDTO.fromEntity(award), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
