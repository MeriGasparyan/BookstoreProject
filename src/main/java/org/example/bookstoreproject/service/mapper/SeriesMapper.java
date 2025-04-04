package org.example.bookstoreproject.service.mapper;


import org.example.bookstoreproject.business.SeriesC;
import org.example.bookstoreproject.persistance.entry.Series;
import org.example.bookstoreproject.service.dto.SeriesDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeriesMapper {

    public SeriesDTO mapToDto(SeriesC series) {
        if (series == null) {
            return null;
        }

        return new SeriesDTO(
                series.getTitle()
        );
    }

    public List<SeriesDTO> mapToDtos(List<SeriesC> list) {
        List<SeriesDTO> dtos = new ArrayList<>();

        for (SeriesC series : list) {
            dtos.add(mapToDto(series));
        }

        return dtos;
    }

    public SeriesC mapDtoToSeries(SeriesDTO dto) {
        return new SeriesC(dto.getTitle());
    }
    public Series mapDtoToEntity(SeriesDTO dto) {
        return new Series(dto.getTitle());
    }
}
