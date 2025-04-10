package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Order(9)
public class BookProcessor implements CSVColumnProcessor {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final SeriesRepository seriesRepository;
    private final LanguageRepository languageRepository;
    private final FormatRepository formatRepository;

    private final DateFormatter dateFormatter;
    private final IntegerFormatter pagesFormatter;
    private final FloatFormatter priceFormatter;
    private final LanguageFormatter languageFormatter;
    private final FormatFormatter formatFormatter;

    @Override
    public void process(List<CSVRow> data) {
        // Cache existing entities to avoid redundant DB queries
        Map<String, Publisher> publisherMap = new HashMap<>();
        Map<String, Series> seriesMap = new HashMap<>();
        Map<String, LanguageEntity> languageMap = new HashMap<>();
        Map<String, FormatEntity> formatMap = new HashMap<>();


        publisherRepository.findAll().forEach(publisher -> publisherMap.put(publisher.getName(), publisher));
        seriesRepository.findAll().forEach(series -> seriesMap.put(series.getTitle(), series));
        languageRepository.findAll().forEach(language -> languageMap.put(language.getLanguage(), language));
        formatRepository.findAll().forEach(format -> formatMap.put(format.getFormat(), format));

        List<Publisher> newPublishersToSave = new ArrayList<>();
        List<Series> newSeriesToSave = new ArrayList<>();
        List<Book> newBooksToSave = new ArrayList<>();

        for (CSVRow row : data) {
            try {
                Optional<Book> existingBook = bookRepository.findByBookID(row.getBookID().trim());
                if (existingBook.isPresent()) {
                    System.out.println("Book already exists: ISBN = " + row.getIsbn().trim() + ", Title = " + row.getTitle().trim());
                    continue;
                }

                Language language = languageFormatter.formatLanguage(row.getLanguage());
                Format format = formatFormatter.formatFormat(row.getFormat());

                LanguageEntity languageEntity = languageMap.computeIfAbsent(language.name(), lang -> {
                    LanguageEntity newLang = new LanguageEntity(lang);
                    return languageRepository.save(newLang);
                });

                FormatEntity formatEntity = formatMap.computeIfAbsent(format.name(), fmt -> {
                    FormatEntity newFormat = new FormatEntity(fmt);
                    return formatRepository.save(newFormat);
                });

                Integer pages = pagesFormatter.getInt(row.getPages());
                Float price = priceFormatter.getFloat(row.getPrice());
                Date publishDate = dateFormatter.getDate(row.getPublishDate());
                Date firstPublishDate = dateFormatter.getDate(row.getFirstPublishDate());

                Publisher publisher = publisherMap.computeIfAbsent(row.getPublisher(), pub -> {
                    Publisher newPublisher = new Publisher(pub);
                    newPublishersToSave.add(newPublisher);
                    return newPublisher;
                });

                Series series = seriesMap.computeIfAbsent(row.getSeries(), ser -> {
                    Series newSeries = new Series(ser);
                    newSeriesToSave.add(newSeries);
                    return newSeries;
                });

                Book book = new Book(
                        row.getTitle(),
                        row.getBookID().trim(),
                        languageEntity,
                        row.getIsbn(),
                        formatEntity,
                        pages,
                        price,
                        publishDate,
                        firstPublishDate,
                        publisher,
                        series
                );
                newBooksToSave.add(book);

                System.out.println("Processed and queued book with ISBN: " + row.getIsbn());

            } catch (Exception e) {
                System.err.println("Error processing row with ISBN: " + row.getIsbn() + ". Error: " + e.getMessage());
            }
        }
        if (!newPublishersToSave.isEmpty()) {
            publisherRepository.saveAll(newPublishersToSave);
        }
        if (!newSeriesToSave.isEmpty()) {
            seriesRepository.saveAll(newSeriesToSave);
        }
        if (!newBooksToSave.isEmpty()) {
            bookRepository.saveAll(newBooksToSave);
        }
    }
}
