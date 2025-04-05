package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Series;
import org.example.bookstoreproject.persistance.repository.SeriesRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.dto.SeriesDTO;
import org.example.bookstoreproject.service.mapper.SeriesMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SeriesProcessor implements CSVColumnProcessor{

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;

    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            if(!row.getSeries().isEmpty()){
                SeriesDTO seriesDto = new SeriesDTO(row.getSeries().trim());
                Optional<Series> existing = seriesRepository.findByTitle(seriesDto.getTitle());
                if (existing.isEmpty()) {
                    Series series = seriesMapper.mapDtoToEntity(seriesDto);
                    seriesRepository.save(series);
                }
            }
        }

        }
}
