package org.example.bookstoreproject.service.columnprocessor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.example.bookstoreproject.persistance.entry.Setting;
import org.example.bookstoreproject.persistance.repository.SettingRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class SettingProcessor {

    private final SettingRepository settingRepository;

    @Transactional
    public Pair<Map<String, Setting>, Map<String, List<Setting>>> process(List<CSVRow> data) {
        Map<String, List<Setting>> settingBookMap = new ConcurrentHashMap<>();
        Map<String, Setting> existingSettingMap = new ConcurrentHashMap<>();
        List<Setting> newSettingsToSave = new CopyOnWriteArrayList<>();

        // Load existing settings once
        List<Setting> settingList = settingRepository.findAll();
        settingList.forEach(setting -> existingSettingMap.put(setting.getName(), setting));

        data.parallelStream().forEach(row -> {
            String[] settingArr = ArrayStringParser.getArrElements(row.getSettings());
            if (settingArr == null) return;

            List<Setting> settingsForBook = new CopyOnWriteArrayList<>();
            for (String settingName : settingArr) {
                String trimmedSettingName = settingName.trim();
                // Atomic get or create
                Setting setting = existingSettingMap.computeIfAbsent(trimmedSettingName, k -> {
                    Setting newSetting = new Setting(trimmedSettingName);
                    newSettingsToSave.add(newSetting);
                    return newSetting;
                });
                settingsForBook.add(setting);
            }
            settingBookMap.compute(row.getBookID().trim(), (bookId, settingListForBook) -> {
                if (settingListForBook == null) {
                    settingListForBook = new CopyOnWriteArrayList<>();
                }
                settingListForBook.addAll(settingsForBook);
                return settingListForBook;
            });
        });

        // Save new settings outside the stream
        if (!newSettingsToSave.isEmpty()) {
            settingRepository.saveAll(newSettingsToSave);
        }
        return Pair.of(existingSettingMap, settingBookMap);
    }
}