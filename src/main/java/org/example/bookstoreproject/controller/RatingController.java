package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.services.UserBookRatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RatingController {
    private final UserBookRatingService ratingService;

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> removeReviewText(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean hasPermission = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("DELETE_ANY_REVIEW"));
        if (hasPermission) {
            ratingService.nullifyReviewText(reviewId);
        }
        ratingService.nullifyReviewText(reviewId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
