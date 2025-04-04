package org.example.bookstoreproject.service;

import org.example.bookstoreproject.service.impl.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
public class ServiceInitializer {

    private final List<Service> services;

    @Autowired
    public ServiceInitializer(List<Service> services) {
        this.services = services;
    }

    public void initializeServices(List<Map<String, String>> csvData) {
        if (csvData == null || csvData.isEmpty()) {
            System.err.println("CSV data is empty. Skipping service initialization.");
            return;
        }
        System.out.println("Number of services: " + services.size());
        for (Service service : services) {
            System.out.println("Service: " + service.getClass().getSimpleName());
        }

        for (Service service : services) {
            if (service instanceof ServiceInterface) {
                ((ServiceInterface) service).process(csvData);
            }
        }

        System.out.println("Services initialized and data passed successfully!");
    }
}
