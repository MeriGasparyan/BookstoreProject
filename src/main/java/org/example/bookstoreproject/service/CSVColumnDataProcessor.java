package org.example.bookstoreproject.service;

import org.example.bookstoreproject.service.columnprocessor.CSVColumnProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CSVColumnDataProcessor {

    private final List<CSVColumnProcessor> services;

    @Autowired
    public CSVColumnDataProcessor(List<CSVColumnProcessor> services) {
        this.services = services;
    }

    public void initializeServices(List<CSVRow> data) {
        if (data == null || data.isEmpty()) {
            System.err.println("CSV data is empty. Skipping service initialization.");
            return;
        }
        System.out.println("Number of services: " + services.size());

        for (CSVColumnProcessor service : services) {
            System.out.println("Service: " + service.getClass().getSimpleName());
            service.process(data);
        }

        System.out.println("Services initialized and data passed successfully!");
    }
}
