package org.example.bookstoreproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Award;
import org.example.bookstoreproject.service.dto.AwardDTO;
import org.example.bookstoreproject.service.dto.CreateAwardDTO;
import org.example.bookstoreproject.service.services.AwardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/awards")
public class AwardController {
    private final AwardService awardService;

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOK_METADATA')")
    public ResponseEntity<AwardDTO> createAward(@RequestBody @Valid CreateAwardDTO awardDTO) {
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
