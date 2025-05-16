package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.dto.MostBoughtBookDTO;
import org.example.bookstoreproject.service.dto.OffensiveReviewDTO;
import org.example.bookstoreproject.service.dto.PageResponseDto;
import org.example.bookstoreproject.service.services.AnalyticsService;
import org.example.bookstoreproject.service.services.OffensiveReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final OffensiveReviewService offensiveReviewService;
    private final AnalyticsService analyticsService;

    @GetMapping("/offensive-reviews")
    @PreAuthorize("hasAuthority('VIEW_FLAGGED_CONTENT')")
    public ResponseEntity<PageResponseDto<OffensiveReviewDTO>> getOffensiveReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<OffensiveReviewDTO> pagedResult = offensiveReviewService.getOffensiveReviews(page, size);
        return ResponseEntity.ok(PageResponseDto.from(pagedResult));
    }

    @GetMapping("/most-bought-books")
    @PreAuthorize("hasAuthority('VIEW_ANALYTICS_DASHBOARD')")
    public ResponseEntity<PageResponseDto<MostBoughtBookDTO>> getMostBoughtBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MostBoughtBookDTO> result = analyticsService.getMostBoughtBooks(page, size);
        return ResponseEntity.ok(PageResponseDto.from(result));
    }
}
