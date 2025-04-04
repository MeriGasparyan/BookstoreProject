package org.example.bookstoreproject.service.utility;

import com.opencsv.CSVReader;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;

@Component
//@Lazy
public class CSVParser {

    private final String sourcePath;
    @Getter
    private final List<Map<String, String>> data = new ArrayList<>();

    private boolean isParsed = false;

    public CSVParser(@Value("${csv.file.path:}") String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public synchronized void init() {
        if (isParsed) return;
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
            isParsed = true;
            System.out.println("CSV data loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        }
    }

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
            isParsed = true;
            System.out.println("CSV data loaded successfully from upload!");
        } catch (Exception e) {
            System.err.println("Error loading CSV from file: " + e.getMessage());
        }
    }
}
