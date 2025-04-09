package org.example.bookstoreproject.service.format;

import org.example.bookstoreproject.enums.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageFormatter {
    public Language formatLanguage(String languageString) {
        return Language.fromString(languageString);
    }
}
