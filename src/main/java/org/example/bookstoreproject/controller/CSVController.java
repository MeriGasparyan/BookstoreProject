package org.example.bookstoreproject.controller;

import org.example.bookstoreproject.service.ColumnsDataProcessor;
import org.example.bookstoreproject.service.utility.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class CSVController {

    @Autowired
    private CSVParser csvParser;

    @Autowired
    private ColumnsDataProcessor serviceInitializer;  // Autowired for automatic injection

    @PostMapping("/csv")
    public ResponseEntity<String> uploadCsv(@RequestParam("books") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(400).body("No file uploaded");
            }
            System.out.println("File uploaded: " + file.getOriginalFilename());


            //csvParser.init();
            //System.out.println("CSV Parser initialized");


            csvParser.parseCSV(file);
            System.out.println("CSV Parsed successfully");

            serviceInitializer.initializeServices(csvParser.getData());
            System.out.println("Services initialized");

            return ResponseEntity.ok("CSV file uploaded, parsed, and processed successfully.");
        } catch (Exception e) {

            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing CSV file: " + e.getMessage());
        }
    }
}
