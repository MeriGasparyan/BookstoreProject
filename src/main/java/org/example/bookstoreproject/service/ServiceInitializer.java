package org.example.bookstoreproject.service;

import org.example.bookstoreproject.service.impl.AuthorService;
import org.example.bookstoreproject.service.utility.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ServiceInitializer {

    private final CSVParser csvParser;
    private final List<Object> services;

    @Autowired
    public ServiceInitializer(CSVParser csvParser, List<Object> services) {
        this.csvParser = csvParser;
        this.services = services;
    }


    public void initializeServices() {
        List<Map<String, String>> csvData = csvParser.getData();

        for (Object service : services) {
            if (service instanceof AuthorService) {
                ((AuthorService) service).processAuthors();
            }
        }

        System.out.println("Services initialized and data passed successfully!");
    }
}

