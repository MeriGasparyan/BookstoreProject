package org.example.bookstoreproject.service.impl;

import jakarta.annotation.PostConstruct;
import org.example.bookstoreproject.service.utility.CSVParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthorService {

    private final CSVParser csvParser;

    public AuthorService(CSVParser csvParser) {
        this.csvParser = csvParser;
    }

    @PostConstruct
    public void init() {
        processAuthors();
    }
    public void processAuthors() {
        List<Map<String, String>> data = csvParser.getData();
        for (Map<String, String> row : data) {
            System.out.println(row.get("author"));
        }
    }
}

