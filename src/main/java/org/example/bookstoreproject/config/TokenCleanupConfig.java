package org.example.bookstoreproject.config;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.repository.RefreshTokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class TokenCleanupConfig {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    @Transactional
    public void cleanExpiredTokens() {
        Instant now = Instant.now();
        int deletedCount = refreshTokenRepository.deleteAllExpiredSince(now);
        System.out.println("Deleted {} expired refresh tokens " + deletedCount);
    }
}
