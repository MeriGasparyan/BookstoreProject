package org.example.bookstoreproject.service;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CSVDataHandler {
    private CSVParser csvParser;
    private CSVColumnDataProcessor csvColumnDataProcessor;

    public void processCSVData(MultipartFile file) {

        List<CSVRow> data = csvParser.parseCSV(file);
        System.out.println("CSV Parsed successfully");

        csvColumnDataProcessor.initializeServices(data);
        System.out.println("Services initialized");

    }
}
