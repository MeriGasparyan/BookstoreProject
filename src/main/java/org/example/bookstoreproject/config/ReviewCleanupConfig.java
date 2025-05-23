package org.example.bookstoreproject.config;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.services.ReviewCleanupService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ReviewCleanupConfig {
    private final ReviewCleanupService reviewCleanupService;

    @Scheduled(fixedRateString = "${cleanup.interval}")
    @Transactional
    public void cleanupRejectedReviews() {
        reviewCleanupService.cleanupRejectedReviews();
    }
}