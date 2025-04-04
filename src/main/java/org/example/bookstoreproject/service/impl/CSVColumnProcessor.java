package org.example.bookstoreproject.service.impl;

import java.util.List;
import java.util.Map;

public interface CSVColumnProcessor {
    void process(List<Map<String, String>> data);
}
