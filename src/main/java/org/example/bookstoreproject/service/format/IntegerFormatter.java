package org.example.bookstoreproject.service.format;

import org.springframework.stereotype.Component;

@Component
public class IntegerFormatter {
    public Integer getInt(String pagesString) {
        if (pagesString == null || pagesString.isEmpty()) {
            return null;
        }
                try {
                    return Integer.parseInt(pagesString);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
}
