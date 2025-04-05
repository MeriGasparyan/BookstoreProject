package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Setting;
import org.example.bookstoreproject.persistance.repository.SettingRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.SettingDTO;
import org.example.bookstoreproject.service.mapper.SettingMapper;
import org.example.bookstoreproject.service.utility.ArrayStringParser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SettingProcessor implements CSVColumnProcessor{
    private final SettingRepository settingRepository;
    private final SettingMapper settingMapper;

    @Override
    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            String[] settingArr = ArrayStringParser.getArrElements(row.getSettings());
            if (settingArr == null)
                continue;
            for (String setting : settingArr) {
                SettingDTO settingDTO = new SettingDTO(setting);
                Optional<Setting> existing = settingRepository.findByName(settingDTO.getName());
                if (existing.isEmpty()) {
                    Setting settingEntity = settingMapper.mapDtoToEntity(settingDTO);
                    settingRepository.save(settingEntity);
                }
            }

        }
    }
}
