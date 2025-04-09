package org.example.bookstoreproject.service.columnprocessor;

import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CSVColumnProcessor {
    void process(List<CSVRow> data);
}
