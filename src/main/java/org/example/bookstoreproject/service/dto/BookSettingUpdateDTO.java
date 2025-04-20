package org.example.bookstoreproject.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSettingUpdateDTO {
    private List<Long> settings;
}
