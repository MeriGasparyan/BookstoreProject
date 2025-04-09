package org.example.bookstoreproject.service.format;

public class PagesFormatter {
    public static Integer getPagesNumber(String pagesString) {
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
