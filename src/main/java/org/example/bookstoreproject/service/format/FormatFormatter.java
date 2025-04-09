package org.example.bookstoreproject.service.format;
import org.example.bookstoreproject.enums.Format;

public class FormatFormatter {
    public static Format formatFormat(String formatString) {
        return Format.fromString(formatString);
    }
}
