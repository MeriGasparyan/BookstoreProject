package org.example.bookstoreproject.service.format;
import org.example.bookstoreproject.enums.Format;
import org.springframework.stereotype.Component;

@Component
public class FormatFormatter {
    public Format formatFormat(String formatString) {
        return Format.fromString(formatString);
    }
}
