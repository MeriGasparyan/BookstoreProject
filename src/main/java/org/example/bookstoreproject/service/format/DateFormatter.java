package org.example.bookstoreproject.service.format;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateFormatter {
    private final String[] formats;

    public DateFormatter(@Value("${csv.date-formats}") String formatString) {
        this.formats = formatString.split(";");
    }

    public Date getDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        dateString = dateString.replaceAll("(?<=\\d)(st|nd|rd|th)", "").trim();
        for (String format : formats) {
            try {
                return new SimpleDateFormat(format).parse(dateString);
            } catch (ParseException ignored) {
            }
        }

        System.err.println("Error parsing date: " + dateString);
        return null;
    }
}
