package org.example.bookstoreproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.persistance.entry.FormatEntity;
import org.example.bookstoreproject.persistance.repository.FormatRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormatSeeder {
    private final FormatRepository formatRepository;

    @PostConstruct
    public void seedFormats() {
        for (Format formatEnum : Format.values()) {
            String formatName = formatEnum.name();
            formatRepository.findByFormat(formatName).orElseGet(() -> {
                FormatEntity newFormat = new FormatEntity(formatName);
                return formatRepository.save(newFormat);
            });
        }

        System.out.println("Format table seeded with enum values!");
    }
}

