package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.MostBoughtBookDTO;
import org.example.bookstoreproject.service.dto.OffensiveReviewDTO;
import org.example.bookstoreproject.service.dto.PageResponseDto;
import org.example.bookstoreproject.service.services.AnalyticsService;
import org.example.bookstoreproject.service.services.OffensiveReviewService;
import org.example.bookstoreproject.service.services.PermissionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final OffensiveReviewService offensiveReviewService;
    private final AnalyticsService analyticsService;
    private final PermissionService permissionService;

    @GetMapping("/offensive-reviews")
    public ResponseEntity<PageResponseDto<OffensiveReviewDTO>> getOffensiveReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        permissionService.hasPermission(user.getId(), "VIEW_FLAGGED_CONTENT");
        Page<OffensiveReviewDTO> pagedResult = offensiveReviewService.getOffensiveReviews(page, size);
        return ResponseEntity.ok(PageResponseDto.from(pagedResult));
    }

    @GetMapping("/most-bought-books")
    public ResponseEntity<PageResponseDto<MostBoughtBookDTO>> getMostBoughtBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        permissionService.hasPermission(user.getId(), "VIEW_ANALYTICS_DASHBOARD");
        Page<MostBoughtBookDTO> result = analyticsService.getMostBoughtBooks(page, size);
        return ResponseEntity.ok(PageResponseDto.from(result));
    }
}
