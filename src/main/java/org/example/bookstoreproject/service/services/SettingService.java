package org.example.bookstoreproject.service.services;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookSetting;
import org.example.bookstoreproject.persistance.entry.Setting;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.BookSettingRepository;
import org.example.bookstoreproject.persistance.repository.SettingRepository;
import org.example.bookstoreproject.service.dto.CreateSettingDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SettingService {
    private final SettingRepository settingRepository;
    private final BookRepository bookRepository;
    private final BookSettingRepository bookSettingRepository;

    @Transactional
    public Setting createSetting(CreateSettingDTO settingDTO) {
        String name = settingDTO.getName();
        if (settingRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Setting with name '" + name + "' already exists");
        }
        Setting setting = new Setting();
        setting.setName(name);
        return settingRepository.save(setting);
    }

    @Transactional
    public Book addSettingsToBook(Long bookId, List<Long> settingIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        List<Setting> settings = settingRepository.findAllById(settingIds);

        if (settings.size() != settingIds.size()) {
            Set<Long> foundSettingIds = settings.stream()
                    .map(Setting::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = settingIds.stream()
                    .filter(id -> !foundSettingIds.contains(id))
                    .toList();

            throw new NoSuchElementException("Setting(s) with ID(s) " + missingIds + " not found");
        }

        settings.forEach(setting -> {
            if (bookSettingRepository.findByBookAndSetting(book, setting).isEmpty()) {
                book.addBookSetting(new BookSetting(book, setting));
            }
        });
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeSettingsFromBook(Long bookId, List<Long> settingIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found"));

        List<Setting> settings = settingRepository.findAllById(settingIds);
        if (settings.size() != settingIds.size()) {
            Set<Long> foundSettingIds = settings.stream()
                    .map(Setting::getId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = settingIds.stream()
                    .filter(id -> !foundSettingIds.contains(id))
                    .toList();

            throw new NoSuchElementException("Setting(s) with ID(s) " + missingIds + " not found");
        }

        settings.forEach(setting -> {
            bookSettingRepository.findByBookAndSetting(book, setting)
                    .ifPresentOrElse(
                            bookSetting -> {
                                book.getBookSettings().remove(bookSetting);
                                bookSettingRepository.delete(bookSetting);
                                bookSetting.setBook(null);
                                bookSetting.setSetting(null);
                            },
                            () -> {
                                throw new IllegalStateException(
                                        "Setting with ID " + setting.getId() +
                                                " is not associated with book " + bookId);
                            }
                    );
        });

        return bookRepository.save(book);
    }
}