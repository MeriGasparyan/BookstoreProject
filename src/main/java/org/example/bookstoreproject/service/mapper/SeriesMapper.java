package org.example.bookstoreproject.service.mapper;


import org.example.bookstoreproject.persistance.entry.Series;
import org.example.bookstoreproject.service.dto.SeriesDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeriesMapper {
    public Series mapDtoToEntity(SeriesDTO dto) {
        return new Series(dto.getTitle());
    }
}
