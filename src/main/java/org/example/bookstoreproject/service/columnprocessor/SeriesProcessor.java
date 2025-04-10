package org.example.bookstoreproject.service.columnprocessor;

import lombok.AllArgsConstructor;
import org.example.bookstoreproject.persistance.entry.Series;
import org.example.bookstoreproject.persistance.repository.SeriesRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
@Order(7)
public class SeriesProcessor implements CSVColumnProcessor {

    private final SeriesRepository seriesRepository;

    @Override
    public void process(List<CSVRow> data) {
        Map<String, Series> existingSeriesMap = new HashMap<>();
        List<Series> seriesList = seriesRepository.findAll();
        for (Series series : seriesList) {
            existingSeriesMap.put(series.getTitle(), series);
        }

        List<Series> newSeriesToSave = new ArrayList<>();
        for (CSVRow row : data) {
            if (!row.getSeries().isEmpty()) {
                String seriesTitle = row.getSeries().trim();
                Series series = existingSeriesMap.get(seriesTitle);
                if (series == null) {
                    series = new Series(seriesTitle);
                    existingSeriesMap.put(seriesTitle, series);
                    newSeriesToSave.add(series);
                }
            }
        }
        if (!newSeriesToSave.isEmpty()) {
            seriesRepository.saveAll(newSeriesToSave);
        }
    }
}
