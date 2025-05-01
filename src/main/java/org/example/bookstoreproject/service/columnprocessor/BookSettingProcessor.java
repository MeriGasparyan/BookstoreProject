package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entity.*;
import org.example.bookstoreproject.persistance.repository.BookSettingRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookSettingProcessor {

    private final BookSettingRepository bookSettingRepository;

    @Transactional
    public void process(Map<String, Book> bookMap, Map<String, List<Setting>> settingBookMap) {
        if (settingBookMap.isEmpty() || bookMap.isEmpty()) {
            return;
        }
        List<BookSetting> existingBookSettings = bookSettingRepository.findAll();

        Set<Pair<Long, Long>> existingPairs = new HashSet<>();
        for (BookSetting bs : existingBookSettings) {
            existingPairs.add(Pair.of(bs.getBook().getId(), bs.getSetting().getId()));
        }

        List<BookSetting> bookSettingsToSave = new ArrayList<>();

        for (Map.Entry<String, List<Setting>> entry : settingBookMap.entrySet()) {
            String bookID = entry.getKey().trim();
            Book book = bookMap.get(bookID);

            if (book == null) {
                continue;
            }

            for (Setting setting : entry.getValue()) {
                Pair<Long, Long> pair = Pair.of(book.getId(), setting.getId());
                if (!existingPairs.contains(pair)) {
                    BookSetting bookSetting = new BookSetting(book, setting);
                    bookSettingsToSave.add(bookSetting);
                    book.addBookSetting(bookSetting);
                    existingPairs.add(pair);
                }
            }
        }

        if (!bookSettingsToSave.isEmpty()) {
            bookSettingRepository.saveAll(bookSettingsToSave);
        }
    }
}
