package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.*;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.BookSettingRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class BookSettingProcessor implements CSVColumnProcessor {

    private final BookSettingRepository bookSettingRepository;
    private final SettingProcessor settingProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Setting>> settingBookMap = settingProcessor.getSettingBookMap();

        List<Book> allBooks = bookRepository.findAll();
        List<BookSetting> existingBookSettings = bookSettingRepository.findAll();

        Map<String, Book> bookMap = new HashMap<>();
        for (Book book : allBooks) {
            bookMap.put(book.getBookID(), book);
        }

        Set<Pair<Long, Long>> existingPairs = new HashSet<>();
        for (BookSetting bs : existingBookSettings) {
            existingPairs.add(Pair.of(bs.getBook().getId(), bs.getSetting().getId()));
        }

        List<BookSetting> bookSettingsToSave = new ArrayList<>();

        for (Map.Entry<String, List<Setting>> entry : settingBookMap.entrySet()) {
            String bookID = entry.getKey().trim();
            Book book = bookMap.get(bookID);

            if (book == null) {
                System.out.println("No book found for bookID: " + bookID);
                continue;
            }

            for (Setting setting : entry.getValue()) {
                Pair<Long, Long> pair = Pair.of(book.getId(), setting.getId());
                if (!existingPairs.contains(pair)) {
                    BookSetting bookSetting = new BookSetting(book, setting);
                    bookSettingsToSave.add(bookSetting);
                    existingPairs.add(pair);
                }
            }
        }

        if (!bookSettingsToSave.isEmpty()) {
            bookSettingRepository.saveAll(bookSettingsToSave);
        }
    }
}
