package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Setting;
import org.example.bookstoreproject.persistance.repository.SettingRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.SettingDTO;
import org.example.bookstoreproject.service.mapper.SettingMapper;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Order(7)
public class SettingProcessor implements CSVColumnProcessor{
    private final SettingRepository settingRepository;

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            String[] settingArr = ArrayStringParser.getArrElements(row.getSettings());
            if (settingArr == null)
                continue;
            for (String setting : settingArr) {
                Optional<Setting> existing = settingRepository.findByName(setting);
                if (existing.isEmpty()) {
                    Setting settingEntity = new Setting(setting);
                    settingRepository.save(settingEntity);
                }
            }

        }
    }
}
