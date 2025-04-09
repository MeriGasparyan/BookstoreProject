package org.example.bookstoreproject.enums;

public enum Language {
    ENGLISH,
    FRENCH,
    GERMAN,
    SPANISH,
    ITALIAN,
    PORTUGUESE,
    RUSSIAN,
    CHINESE,
    JAPANESE,
    KOREAN,
    HINDI,
    ARABIC,
    TURKISH,
    ARMENIAN,
    GEORGIAN,
    GREEK,
    DUTCH,
    SWEDISH,
    NORWEGIAN,
    DANISH,
    FINNISH,
    POLISH,
    UKRAINIAN,
    HUNGARIAN,
    CZECH,
    SLOVAK,
    ROMANIAN,
    BULGARIAN,
    CROATIAN,
    SERBIAN,
    SLOVENIAN,
    THAI,
    VIETNAMESE,
    INDONESIAN,
    MALAY,
    TAMIL,
    TELUGU,
    BENGALI,
    URDU,
    HEBREW,
    PERSIAN,
    PASHTO,
    SWAHILI,
    FILIPINO,
    LATIN,
    ICELANDIC,
    WELSH,
    IRISH,
    UNKNOWN;

    public static Language fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return UNKNOWN;
        }
        return switch (str.trim().toLowerCase()) {
            case "english" -> ENGLISH;
            case "french" -> FRENCH;
            case "german" -> GERMAN;
            case "spanish" -> SPANISH;
            case "italian" -> ITALIAN;
            case "portuguese" -> PORTUGUESE;
            case "russian" -> RUSSIAN;
            case "chinese" -> CHINESE;
            case "japanese" -> JAPANESE;
            case "korean" -> KOREAN;
            case "hindi" -> HINDI;
            case "arabic" -> ARABIC;
            case "turkish" -> TURKISH;
            case "armenian" -> ARMENIAN;
            case "georgian" -> GEORGIAN;
            case "greek" -> GREEK;
            case "dutch" -> DUTCH;
            case "swedish" -> SWEDISH;
            case "norwegian" -> NORWEGIAN;
            case "danish" -> DANISH;
            case "finnish" -> FINNISH;
            case "polish" -> POLISH;
            case "ukrainian" -> UKRAINIAN;
            case "hungarian" -> HUNGARIAN;
            case "czech" -> CZECH;
            case "slovak" -> SLOVAK;
            case "romanian" -> ROMANIAN;
            case "bulgarian" -> BULGARIAN;
            case "croatian" -> CROATIAN;
            case "serbian" -> SERBIAN;
            case "slovenian" -> SLOVENIAN;
            case "thai" -> THAI;
            case "vietnamese" -> VIETNAMESE;
            case "indonesian" -> INDONESIAN;
            case "malay" -> MALAY;
            case "tamil" -> TAMIL;
            case "telugu" -> TELUGU;
            case "bengali" -> BENGALI;
            case "urdu" -> URDU;
            case "hebrew" -> HEBREW;
            case "persian" -> PERSIAN;
            case "pashto" -> PASHTO;
            case "swahili" -> SWAHILI;
            case "filipino" -> FILIPINO;
            case "latin" -> LATIN;
            case "icelandic" -> ICELANDIC;
            case "welsh" -> WELSH;
            case "irish" -> IRISH;
            default -> UNKNOWN;
        };
    }
}
