package org.example.bookstoreproject.enums;

public enum Format {
    HARDCOVER,
    PAPERBACK,
    MASS_MARKET_PAPERBACK,
    TRADE_PAPERBACK,
    KINDLE_EDITION,
    SPIRAL_BOUND,
    LIBRARY_BINDING,
    BOARD_BOOK,
    AUDIOBOOK_CD,
    AUDIOBOOK_CASSETTE,
    AUDIOBOOK_MP3,
    AUDIOBOOK_DOWNLOAD,
    EBOOK,
    PDF,
    EPUB,
    MOBI,
    KINDLE,
    NOOK,
    ONLINE,
    BOX_SET,
    COMIC,
    GRAPHIC_NOVEL,
    JOURNAL,
    MAGAZINE,
    NEWSPAPER,
    POSTER,
    MAP,
    CALENDAR,
    FLASHCARDS,
    WORKBOOK,
    UNKNOWN;
    public static Format fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return UNKNOWN;
        }
        return switch (str.trim().toUpperCase().replace(" ", "_")) {
            case "HARDCOVER" -> HARDCOVER;
            case "PAPERBACK" -> PAPERBACK;
            case "MASS_MARKET_PAPERBACK" -> MASS_MARKET_PAPERBACK;
            case "TRADE_PAPERBACK" -> TRADE_PAPERBACK;
            case "KINDLE_EDITION" -> KINDLE_EDITION;
            case "SPIRAL_BOUND" -> SPIRAL_BOUND;
            case "LIBRARY_BINDING" -> LIBRARY_BINDING;
            case "BOARD_BOOK" -> BOARD_BOOK;
            case "AUDIOBOOK_CD" -> AUDIOBOOK_CD;
            case "AUDIOBOOK_CASSETTE" -> AUDIOBOOK_CASSETTE;
            case "AUDIOBOOK_MP3" -> AUDIOBOOK_MP3;
            case "AUDIOBOOK_DOWNLOAD" -> AUDIOBOOK_DOWNLOAD;
            case "EBOOK" -> EBOOK;
            case "PDF" -> PDF;
            case "EPUB" -> EPUB;
            case "MOBI" -> MOBI;
            case "KINDLE" -> KINDLE;
            case "NOOK" -> NOOK;
            case "ONLINE" -> ONLINE;
            case "BOX_SET" -> BOX_SET;
            case "COMIC" -> COMIC;
            case "GRAPHIC_NOVEL" -> GRAPHIC_NOVEL;
            case "JOURNAL" -> JOURNAL;
            case "MAGAZINE" -> MAGAZINE;
            case "NEWSPAPER" -> NEWSPAPER;
            case "POSTER" -> POSTER;
            case "MAP" -> MAP;
            case "CALENDAR" -> CALENDAR;
            case "FLASHCARDS" -> FLASHCARDS;
            case "WORKBOOK" -> WORKBOOK;
            default -> UNKNOWN;
        };
    }

}
