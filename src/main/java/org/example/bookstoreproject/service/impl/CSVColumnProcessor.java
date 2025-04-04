package org.example.bookstoreproject.service.impl;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface CSVColumnProcessor {
    void process(List<Map<String, String>> data);
}
