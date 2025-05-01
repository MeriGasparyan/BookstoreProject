package org.example.bookstoreproject.service.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookstoreproject.persistance.entity.Setting;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettingDTO {
    private Long id;
    private String name;

    public static SettingDTO fromEntity(Setting setting) {
        SettingDTO settingDTO = new SettingDTO();
        settingDTO.setId(setting.getId());
        settingDTO.setName(setting.getName());
        return settingDTO;
    }
}
