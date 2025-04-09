package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookSetting;
import org.example.bookstoreproject.persistance.entry.Setting;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.BookSettingRepository;
import org.example.bookstoreproject.service.CSVRow;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookSettingProcessor implements CSVColumnProcessor {
    private final BookSettingRepository bookSettingRepository;
    private final SettingProcessor settingProcessor;
    private final BookRepository bookRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, List<Setting>> genreBookMap =
                settingProcessor.getSettingBookMap();
        for(Map.Entry<String, List<Setting>> entry : genreBookMap.entrySet()){
            List<Setting> settings = entry.getValue();
            Optional<Book> book =
                    bookRepository.findByBookID(entry.getKey().trim());
            if(book.isPresent()){
                for(Setting setting : settings){
                    if(bookSettingRepository.existsByBookAndSetting(book.get(), setting)){
                        continue;
                    }
                    BookSetting bookSetting = new BookSetting(book.get(), setting);
                    bookSettingRepository.save(bookSetting);
                }

            }
        }

    }
}

