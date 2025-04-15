package org.example.bookstoreproject.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.service.utility.CSVParser;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
@Getter
@Setter
@AllArgsConstructor
public class CSVDataHandler {
    private final CSVParser csvParser;
    private final CSVColumnDataProcessor csvColumnDataProcessor;
    private final ExecutorService executorService;

    public void processCSVData(MultipartFile file) {
        List<CSVRow> data = csvParser.parseCSV(file);
        System.out.println("CSV Parsed successfully");

        csvColumnDataProcessor.processColumns(data);
        System.out.println("Services initialized");
    }
}