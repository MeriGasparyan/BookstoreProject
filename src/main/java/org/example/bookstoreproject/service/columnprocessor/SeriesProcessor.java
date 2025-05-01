package org.example.bookstoreproject.service.columnprocessor;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Series;
import org.example.bookstoreproject.persistance.repository.SeriesRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class SeriesProcessor {

    private final SeriesRepository seriesRepository;

    @Transactional
    public Map<String, Series> process(List<CSVRow> data) {
        Map<String, Series> existingSeriesMap = new ConcurrentHashMap<>();
        List<Series> newSeriesToSave = new CopyOnWriteArrayList<>();

        List<Series> seriesList = seriesRepository.findAll();
        seriesList.forEach(series -> existingSeriesMap.put(series.getTitle(), series));

        data.parallelStream().forEach(row -> {
            if (!row.getSeries().isEmpty()) {
                String seriesTitle = row.getSeries().trim();
                Series series = existingSeriesMap.computeIfAbsent(seriesTitle, k -> {
                    Series newSeries = new Series(seriesTitle);
                    newSeriesToSave.add(newSeries);
                    return newSeries;
                });
            }
        });

        if (!newSeriesToSave.isEmpty()) {
            seriesRepository.saveAll(newSeriesToSave);
        }
        return existingSeriesMap;
    }
}