package org.example.bookstoreproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entry.LanguageEntity;
import org.example.bookstoreproject.persistance.repository.LanguageRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LanguageSeeder {
    private final LanguageRepository languageRepository;

    @PostConstruct
    public void seedLanguages() {
        for (Language languageEnum : Language.values()) {
            String languageName = languageEnum.name();

            languageRepository.findByLanguage(languageName).orElseGet(() -> {
                LanguageEntity newLanguage = new LanguageEntity(languageName);
                return languageRepository.save(newLanguage);
            });
        }

        System.out.println("Language table seeded with enum values!");
    }
}
