package org.example.bookstoreproject.service;

import org.example.bookstoreproject.service.impl.CSVColumnProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
public class ServiceProccessor {

    private final List<Service> services;

    @Autowired
    public ServiceProccessor(List<Service> services) {
        this.services = services;
    }

    public void initializeServices(List<Map<String, String>> csvData) {
        if (csvData == null || csvData.isEmpty()) {
            System.err.println("CSV data is empty. Skipping service initialization.");
            return;
        }
        System.out.println("Number of services: " + services.size());

        for (Service service : services) {
            if (service instanceof CSVColumnProcessor) {
                System.out.println("Service: " + service.getClass().getSimpleName());
                ((CSVColumnProcessor) service).process(csvData);
            }
        }

        System.out.println("Services initialized and data passed successfully!");
    }
}
