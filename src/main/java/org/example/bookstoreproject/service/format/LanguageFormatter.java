package org.example.bookstoreproject.service.format;

import org.example.bookstoreproject.enums.Language;

public class LanguageFormatter {
    public static Language formatLanguage(String languageString) {
        return Language.fromString(languageString);
    }
}
