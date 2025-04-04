package org.example.bookstoreproject.service;

import org.example.bookstoreproject.service.impl.CSVColumnProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ColumnsDataProcessor {

    private final List<CSVColumnProcessor> services;

    @Autowired
    public ColumnsDataProcessor(List<CSVColumnProcessor> services) {
        this.services = services;
    }

    public void initializeServices(List<Map<String, String>> csvData) {
        if (csvData == null || csvData.isEmpty()) {
            System.err.println("CSV data is empty. Skipping service initialization.");
            return;
        }
        System.out.println("Number of services: " + services.size());

        for (CSVColumnProcessor service : services) {
            System.out.println("Service: " + service.getClass().getSimpleName());
            service.process(csvData);
        }

        System.out.println("Services initialized and data passed successfully!");
    }
}
