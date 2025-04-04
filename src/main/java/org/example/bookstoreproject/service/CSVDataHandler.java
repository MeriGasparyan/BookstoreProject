package org.example.bookstoreproject.service;

import lombok.Getter;
import lombok.Setter;
import org.example.bookstoreproject.service.utility.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Getter
@Setter
public class CSVDataHandler {
    @Autowired
    private CSVParser csvParser;

    @Autowired
    private CSVColumnDataProcessor csvColumnDataProcessor;

    public void processCSVData(MultipartFile file) {

        List<CSVRow> data = csvParser.parseCSV(file);
        System.out.println("CSV Parsed successfully");

        csvColumnDataProcessor.initializeServices(data);
        System.out.println("Services initialized");

    }
}
