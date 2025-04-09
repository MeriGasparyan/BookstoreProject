package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bookstoreproject.persistance.entry.Author;
import org.example.bookstoreproject.persistance.entry.Setting;
import org.example.bookstoreproject.persistance.repository.SettingRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(7)
public class SettingProcessor implements CSVColumnProcessor{
    private final SettingRepository settingRepository;
    @Getter
    private final Map<String, List<Setting>> settingBookMap;

    public SettingProcessor(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
        this.settingBookMap = new HashMap<>();
    }

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            String[] settingArr = ArrayStringParser.getArrElements(row.getSettings());
            if (settingArr == null)
                continue;
            List<Setting> settings = new ArrayList<>();
            for (String setting : settingArr) {
                Optional<Setting> existing = settingRepository.findByName(setting);
                if (existing.isEmpty()) {
                    Setting settingEntity = new Setting(setting);
                    settings.add(settingEntity);
                    settingRepository.save(settingEntity);
                }
            }
            settingBookMap.put(row.getBookID().trim(), settings);
        }
    }
}
