package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.Format;
import org.example.bookstoreproject.enums.Language;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.*;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.format.DateFormatter;
import org.example.bookstoreproject.service.format.FormatFormatter;
import org.example.bookstoreproject.service.format.LanguageFormatter;
import org.example.bookstoreproject.service.format.IntegerFormatter;
import org.example.bookstoreproject.service.format.FloatFormatter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookProcessor{
    private final BookRepository bookRepository;
    private final LanguageRepository languageRepository;
    private final FormatRepository formatRepository;

    private final DateFormatter dateFormatter;
    private final IntegerFormatter integerFormatter;
    private final FloatFormatter priceFormatter;
    private final LanguageFormatter languageFormatter;
    private final FormatFormatter formatFormatter;

    public Map<String, Book> process(List<CSVRow> data, Map<String, Publisher> publisherMap, Map<String, Series> seriesMap) {
        Map<String, LanguageEntity> languageMap = new HashMap<>();
        Map<String, FormatEntity> formatMap = new HashMap<>();
        Map<String, Book> bookMap = new HashMap<>();

        languageRepository.findAll().forEach(language -> languageMap.put(language.getLanguage(), language));
        formatRepository.findAll().forEach(format -> formatMap.put(format.getFormat(), format));
        bookRepository.findAll().forEach(book -> bookMap.put(book.getBookID(), book));
        List<Book> newBooksToSave = new ArrayList<>();

        for (CSVRow row : data) {
            try {

                if (bookMap.containsKey(row.getBookID())) {
                    System.out.println("Book already exists: BookID = " + row.getBookID().trim() + ", Title = " + row.getTitle().trim());
                    continue;
                }

                Language language = languageFormatter.formatLanguage(row.getLanguage());
                Format format = formatFormatter.formatFormat(row.getFormat());

                LanguageEntity languageEntity = languageMap.get(language.name());

                FormatEntity formatEntity = formatMap.get(format.name());

                Integer pages = integerFormatter.getInt(row.getPages());
                Float price = priceFormatter.getFloat(row.getPrice());
                Date publishDate = dateFormatter.getDate(row.getPublishDate());
                Date firstPublishDate = dateFormatter.getDate(row.getFirstPublishDate());
                Integer bbeScore = integerFormatter.getInt(row.getBbeScore());
                Integer bbeVotes = integerFormatter.getInt(row.getBbeVotes());

                Publisher publisher;
                if(row.getPublisher().trim().isEmpty()){
                    publisher = null;
                }else
                    publisher = publisherMap.get(row.getPublisher().trim());

                Series series;
                if(row.getSeries().trim().isEmpty()){
                    series = null;
                }else
                    series = seriesMap.get(row.getSeries().trim());
                Book book = new Book();
                book.setBookID(row.getBookID().trim());
                book.setTitle(row.getTitle().trim());
                book.setPublisher(publisher);
                book.setSeries(series);
                book.setPublishDate(publishDate);
                book.setFirstPublishDate(firstPublishDate);
                book.setPages(pages);
                book.setPrice(price);
                book.setTitle(row.getTitle().trim());
                book.setBbeScore(bbeScore);
                book.setBbeVotes(bbeVotes);
                book.setLanguage(languageEntity);
                book.setFormat(formatEntity);
                book.setIsbn(row.getIsbn() != null ? row.getIsbn().trim() : null);

                newBooksToSave.add(book);
                bookMap.put(book.getBookID(), book);


            } catch (Exception e) {
                System.err.println("Error processing row with ISBN: " + row.getIsbn() + ". Error: " + e.getMessage());
            }
        }
        if (!newBooksToSave.isEmpty()) {
            bookRepository.saveAll(newBooksToSave);
        }
        return bookMap;
    }
}
