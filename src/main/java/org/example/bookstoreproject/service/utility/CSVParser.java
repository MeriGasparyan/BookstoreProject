package org.example.bookstoreproject.service.utility;

import com.opencsv.CSVReader;
import lombok.Getter;
import org.example.bookstoreproject.service.CSVRow;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStreamReader;
import java.util.*;

@Component
//@Lazy
public class CSVParser {

    public List<CSVRow> parseCSV(MultipartFile file) {
        List<CSVRow> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            reader.readNext();
            String[] values;
            while ((values = reader.readNext()) != null) {
                CSVRow rowData = new CSVRow(values);
                data.add(rowData);
            }
            System.out.println("CSV data loaded successfully from upload!");
        } catch (Exception e) {
            System.err.println("Error loading CSV from file: " + e.getMessage());
        }
        return data;
    }
}
