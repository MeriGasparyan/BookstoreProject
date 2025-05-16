package org.example.bookstoreproject.config;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.services.ImageDownloadService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ImageProcessingConfig {
    private final ImageDownloadService downloadJobService;

    @Scheduled(fixedDelayString = "${image.download.job.interval}")
    @Transactional
    public void runDownloadJob() {
        downloadJobService.processPendingDownloads();
    }
}
