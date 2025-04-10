package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Series;
import org.example.bookstoreproject.persistance.repository.SeriesRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Order(7)
public class SeriesProcessor implements CSVColumnProcessor{

    private final SeriesRepository seriesRepository;

    public void process(List<CSVRow> data) {
        for (CSVRow row : data) {
            if(!row.getSeries().isEmpty()){
                String series = row.getSeries().trim();
                Optional<Series> existing = seriesRepository.findByTitle(series);
                if (existing.isEmpty()) {
                    Series seriesEntity = new Series(series);
                    seriesRepository.save(seriesEntity);
                }
            }
        }

        }
}
