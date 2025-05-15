package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.dto.OffensiveReviewDTO;
import org.example.bookstoreproject.service.dto.PageResponseDto;
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

    @GetMapping("/offensive-reviews")
    @PreAuthorize("hasAuthority('VIEW_FLAGGED_CONTENT')")
    public ResponseEntity<PageResponseDto<OffensiveReviewDTO>> getOffensiveReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<OffensiveReviewDTO> pagedResult = offensiveReviewService.getOffensiveReviews(page, size);
        return ResponseEntity.ok(PageResponseDto.from(pagedResult));
    }
}
