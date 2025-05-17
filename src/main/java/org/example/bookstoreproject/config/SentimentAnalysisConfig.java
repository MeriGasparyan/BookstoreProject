package org.example.bookstoreproject.config;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.services.BatchSentimentAnalysisService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SentimentAnalysisConfig {
    private final BatchSentimentAnalysisService batchSentimentAnalysisService;

    @Scheduled(fixedRateString = "${sentiment.analysis.interval:3600000}")
    @Transactional
    public void processReviewsBatch() {
        batchSentimentAnalysisService.processReviewsBatch();
    }
}