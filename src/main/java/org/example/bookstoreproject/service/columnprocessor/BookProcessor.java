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

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        for (CSVRow row : data) {
            try {
                Optional<Book> existingBook =
                        bookRepository.findByBookID(row.getBookID().trim());
                if (existingBook.isPresent()) {
                    System.out.println("Book already exists: ISBN = "
                            + row.getIsbn().trim() + ", Title = " + row.getTitle().trim());
                }

                Language language = languageFormatter.formatLanguage(row.getLanguage());
                Optional<LanguageEntity> existingLanguage = languageRepository.findByLanguage(language.name());
                Format format = formatFormatter.formatFormat(row.getFormat());
                Optional<FormatEntity> existingFormat = formatRepository.findByFormat(format.name());

                Integer pages = pagesFormatter.getInt(row.getPages());
                Float price = priceFormatter.getFloat(row.getPrice());
                Date publishDate = dateFormatter.getDate(row.getPublishDate());
                Date firstPublishDate = dateFormatter.getDate(row.getFirstPublishDate());

                Publisher publisher = publisherRepository.findByName(row.getPublisher())
                        .orElseGet(() -> {
                            Publisher newPublisher = new Publisher(row.getPublisher());
                            return publisherRepository.save(newPublisher);
                        });

                Series series = seriesRepository.findByTitle(row.getSeries())
                        .orElseGet(() -> {
                            Series newSeries = new Series(row.getSeries());
                            return seriesRepository.save(newSeries);
                        });


                Book book = new Book(
                        row.getTitle(),
                        row.getBookID().trim(),
                        existingLanguage.get(),
                        row.getIsbn(),
                        existingFormat.get(),
                        pages,
                        price,
                        publishDate,
                        firstPublishDate,
                        publisher,
                        series
                );


                bookRepository.save(book);
                System.out.println("Processed and saved book with ISBN: " + row.getIsbn());

            } catch (Exception e) {
                System.err.println("Error processing row with ISBN: " + row.getIsbn() + ". Error: " + e.getMessage());

            }
        }
    }
}