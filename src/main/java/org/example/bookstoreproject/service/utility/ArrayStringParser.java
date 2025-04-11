package org.example.bookstoreproject.service.utility;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

public class ArrayStringParser {

    public static String[] getArrElements(String input) {
        if (input == null) {
            return null;
        }

        String trimmed = input.trim();
        if (trimmed.length() < 2) {
            return null;
        }

        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            if (trimmed.length() <= 2) return null;
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }

        Matcher matcher = Pattern.compile("(['\"])(.*?)\\1|([^,]+)").matcher(trimmed);
        List<String> elements = new ArrayList<>();

        while (matcher.find()) {
            String quoted = matcher.group(2);
            String unquoted = matcher.group(3);
            String raw = quoted != null ? quoted : unquoted;

            if (raw != null) {
                String clean = raw.trim();
                if ((clean.startsWith("'") && clean.endsWith("'")) ||
                        (clean.startsWith("\"") && clean.endsWith("\""))) {
                    if (clean.length() > 1) {
                        clean = clean.substring(1, clean.length() - 1).trim();
                    } else {
                        continue;
                    }
                }
                if (!clean.isEmpty()) {
                    elements.add(clean);
                }
            }
        }

        return elements.isEmpty() ? null : elements.toArray(new String[0]);
    }
}
