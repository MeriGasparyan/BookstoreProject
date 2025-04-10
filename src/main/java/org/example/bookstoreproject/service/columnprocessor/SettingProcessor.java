package org.example.bookstoreproject.service.columnprocessor;

import lombok.Getter;
import org.example.bookstoreproject.persistance.entry.Setting;
import org.example.bookstoreproject.persistance.repository.SettingRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(8)
@Getter
public class SettingProcessor implements CSVColumnProcessor {

    private final SettingRepository settingRepository;
    private final Map<String, List<Setting>> settingBookMap;

    public SettingProcessor(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
        this.settingBookMap = new HashMap<>();
    }

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Setting> existingSettingMap = new HashMap<>();
        List<Setting> settingList = settingRepository.findAll();
        for (Setting setting : settingList) {
            existingSettingMap.put(setting.getName(), setting);
        }

        List<Setting> newSettingsToSave = new ArrayList<>();
        for (CSVRow row : data) {
            String[] settingArr = ArrayStringParser.getArrElements(row.getSettings());
            if (settingArr == null)
                continue;

            List<Setting> settings = new ArrayList<>();
            for (String settingName : settingArr) {
                Setting setting = existingSettingMap.get(settingName);
                if (setting == null) {
                    setting = new Setting(settingName);
                    existingSettingMap.put(settingName, setting);
                    newSettingsToSave.add(setting);
                }
                settings.add(setting);
            }
            settingBookMap.put(row.getBookID().trim(), settings);
        }
        if (!newSettingsToSave.isEmpty()) {
            settingRepository.saveAll(newSettingsToSave);
        }
    }
}
