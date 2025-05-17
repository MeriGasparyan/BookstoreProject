package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.criteria.BookSearchCriteria;
import org.example.bookstoreproject.service.dto.OffensiveReviewDTO;
import org.example.bookstoreproject.service.services.ModerationService;
import org.example.bookstoreproject.service.services.PermissionService;
import org.example.bookstoreproject.service.services.UserBookRatingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RatingController {
    private final UserBookRatingService ratingService;
    private final PermissionService permissionService;
    private final ModerationService moderationService;

    @GetMapping("/moderation/pending")
    public ResponseEntity<Page<OffensiveReviewDTO>> getPendingReviews(
            @ModelAttribute BookSearchCriteria criteria,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        permissionService.checkPermission(userDetails, "VIEW_FLAGGED_CONTENT");

        return ResponseEntity.ok(moderationService.getPendingReviews(criteria.getPage(), criteria.getSize()));
    }

    @PostMapping("/moderation/approve/{reviewId}")
    public ResponseEntity<Void> approveReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        permissionService.checkPermission(userDetails, "APPROVE_REVIEW");
        moderationService.approveReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/moderation/reject/{reviewId}")
    public ResponseEntity<Void> rejectReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        permissionService.checkPermission(userDetails, "FLAG_REVIEW_CONTENT");
        moderationService.rejectReview(reviewId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> removeReviewText(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean hasPermission = permissionService.getPermissionsForUser(userDetails.getId()).stream()
                .anyMatch(auth -> auth.equals("DELETE_ANY_REVIEW"));
        if (hasPermission) {
            ratingService.nullifyReviewText(reviewId);
        }
        ratingService.nullifyReviewText(reviewId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
