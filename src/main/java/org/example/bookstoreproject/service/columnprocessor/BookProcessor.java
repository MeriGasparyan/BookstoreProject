package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.DateFormatter;
import org.example.bookstoreproject.service.format.FormatFormatter;
import org.example.bookstoreproject.service.format.LanguageFormatter;
import org.example.bookstoreproject.service.format.IntegerFormatter;
import org.example.bookstoreproject.service.format.FloatFormatter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookProcessor {

    private final BookRepository bookRepository;
    private final DateFormatter dateFormatter;
    private final IntegerFormatter integerFormatter;
    private final FloatFormatter priceFormatter;
    private final LanguageFormatter languageFormatter;
    private final FormatFormatter formatFormatter;

    @Transactional
    public Map<String, Book> process(List<CSVRow> data, Map<String, Publisher> publisherMap, Map<String, Series> seriesMap) {

        Map<String, Book> newBookMap = new HashMap<>();

        Set<String> existingBooks = new HashSet<>(bookRepository.findAllBookIds());

        for (CSVRow row : data) {
            try {
                String bookId = row.getBookID().trim();
                if (existingBooks.contains(bookId)) {
                    continue;
                }

                String title = row.getTitle().trim();
                String seriesString = row.getSeries().trim();
                String publisherString = row.getPublisher().trim();
                String isbn = row.getIsbn() != null ? row.getIsbn().trim() : null;

                Language language = languageFormatter.formatLanguage(row.getLanguage());
                Format format = formatFormatter.formatFormat(row.getFormat());

                Integer pages = integerFormatter.getInt(row.getPages());
                Float price = priceFormatter.getFloat(row.getPrice());
                Date publishDate = row.getPublishDate().isEmpty() ? null : dateFormatter.getDate(row.getPublishDate());
                Date firstPublishDate = row.getFirstPublishDate().isEmpty() ? null : dateFormatter.getDate(row.getFirstPublishDate());
                Integer bbeScore = integerFormatter.getInt(row.getBbeScore());
                Integer bbeVotes = integerFormatter.getInt(row.getBbeVotes());

                Publisher publisher = publisherString.isEmpty() ? null : publisherMap.get(publisherString);
                Series series = seriesString.isEmpty() ? null : seriesMap.get(seriesString);

                Book book = new Book();
                book.setBookID(bookId);
                book.setTitle(title);
                book.setPublisher(publisher);
                book.setSeries(series);
                book.setPublishDate(publishDate);
                book.setFirstPublishDate(firstPublishDate);
                book.setPages(pages);
                book.setPrice(price);
                book.setBbeScore(bbeScore);
                book.setBbeVotes(bbeVotes);
                book.setLanguage(language);
                book.setFormat(format);
                if (isbn != null && !isbn.isEmpty()) {
                    book.setIsbn(isbn);
                }

                newBookMap.put(bookId, book);
                existingBooks.add(bookId);

            } catch (Exception e) {
                System.err.println("Error processing row with ISBN: " + row.getIsbn() + ". Error: " + e.getMessage());
            }
        }
        if (!newBookMap.isEmpty()) {
            bookRepository.saveAll(newBookMap.values());
        }

        return newBookMap;
    }
}
