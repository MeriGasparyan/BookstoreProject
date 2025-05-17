package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.dto.MostBoughtBookDTO;
import org.example.bookstoreproject.service.dto.PageResponseDto;
import org.example.bookstoreproject.service.services.AnalyticsService;
import org.example.bookstoreproject.service.services.PermissionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final AnalyticsService analyticsService;
    private final PermissionService permissionService;

    @GetMapping("/most-bought-books")
    public ResponseEntity<PageResponseDto<MostBoughtBookDTO>> getMostBoughtBooks(
            @ModelAttribute BookSearchCriteria criteria,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        permissionService.hasPermission(user.getId(), "VIEW_ANALYTICS_DASHBOARD");
        Page<MostBoughtBookDTO> result = analyticsService.getMostBoughtBooks(criteria.getPage(), criteria.getSize());
        return ResponseEntity.ok(PageResponseDto.from(result));
    }
}
