package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.dto.CreateUserReturnDTO;
import org.example.bookstoreproject.service.services.ArtificialDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final ArtificialDataService artificialDataService;

    @PostMapping("/admin/setup")
    public ResponseEntity<CreateUserReturnDTO> setupAdmin() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artificialDataService.setUpAdmin());
    }

    @PostMapping("/generate-users")
    public ResponseEntity<String> generateFakeUsers() {
        artificialDataService.generateFakeUsers(500);
        return ResponseEntity.ok("500 fake users generated");
    }
    @PostMapping("/generate-ratings")
    public ResponseEntity<String> generateFakeRatings() {
        artificialDataService.seedRatings();
        return ResponseEntity.ok("Fake ratings generated");
    }

    @PostMapping("/generate-purchases")
    public ResponseEntity<String> generateFakePurchases() {
        artificialDataService.generateFakePurchases(100);
        return ResponseEntity.ok("100 fake purchases generated");
    }
}
