package org.example.bookstoreproject.service.utility;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.*;

@Component
public class CSVParser {

    private final String sourcePath;
    @Getter
    private List<Map<String, String>> data = new ArrayList<>();

    public CSVParser(@Value("${csv.file.path}") String sourcePath) {
        this.sourcePath = sourcePath;
    }

    @PostConstruct
    public void init() {
        try (CSVReader reader = new CSVReader(new FileReader(sourcePath))) {
            String[] headers = reader.readNext();
            String[] values;

            while ((values = reader.readNext()) != null) {
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], values[i]);
                }
                data.add(row);
            }
            System.out.println("CSV data loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        }
    }

}
