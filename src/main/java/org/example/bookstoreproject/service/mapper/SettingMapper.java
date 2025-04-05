package org.example.bookstoreproject.service.mapper;

import org.example.bookstoreproject.persistance.entry.Setting;
import org.example.bookstoreproject.service.dto.SettingDTO;
import org.springframework.stereotype.Component;

@Component
public class SettingMapper {
    public Setting mapDtoToEntity(SettingDTO dto) {
        return new Setting(dto.getName());
    }
}
