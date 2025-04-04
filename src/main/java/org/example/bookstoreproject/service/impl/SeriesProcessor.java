package org.example.bookstoreproject.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Genre;
import org.example.bookstoreproject.persistance.entry.Series;
import org.example.bookstoreproject.persistance.repository.SeriesRepository;
import org.example.bookstoreproject.service.dto.SeriesDTO;
import org.example.bookstoreproject.service.mapper.SeriesMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SeriesProcessor implements CSVColumnProcessor{

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;

    public void process(List<Map<String, String>> data) {
        System.out.println("Series processing started!"+ seriesRepository);
        for (Map<String, String> row : data) {
            if(!row.get("series").isEmpty()){
                SeriesDTO seriesDto = new SeriesDTO(row.get("series").trim());
                Optional<Series> existing = seriesRepository.findByTitle(seriesDto.getTitle());
                if (existing.isEmpty()) {
                    Series series = seriesMapper.mapDtoToEntity(seriesDto);
                    seriesRepository.save(series);
                }
            }
        }

        }
}
