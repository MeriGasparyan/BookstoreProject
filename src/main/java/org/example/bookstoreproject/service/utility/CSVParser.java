package org.example.bookstoreproject.service.utility;

import com.opencsv.CSVReader;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStreamReader;
import java.util.*;

@Component
//@Lazy
public class CSVParser {

    @Getter
    private final List<Map<String, String>> data = new ArrayList<>();

    public void parseCSV(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] headers = reader.readNext();
            String[] values;

            while ((values = reader.readNext()) != null) {
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], values[i]);
                }
                data.add(row);
            }
            System.out.println("CSV data loaded successfully from upload!");
        } catch (Exception e) {
            System.err.println("Error loading CSV from file: " + e.getMessage());
        }
    }
}
