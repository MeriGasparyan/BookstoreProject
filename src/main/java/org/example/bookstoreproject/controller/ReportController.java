package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.dto.OffensiveReviewDTO;
import org.example.bookstoreproject.service.services.OffensiveReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final OffensiveReviewService offensiveReviewService;

    @GetMapping("/offensive-reviews")
    @PreAuthorize("hasAuthority('VIEW_FLAGGED_CONTENT')")
    public ResponseEntity<List<OffensiveReviewDTO>> getOffensiveReviews() {
        return ResponseEntity.ok(offensiveReviewService.getOffensiveReviews());
    }
}
