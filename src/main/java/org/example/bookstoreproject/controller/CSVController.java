package org.example.bookstoreproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.service.CSVDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/csv")
public class CSVController {

    private final CSVDataHandler csvDataHandler;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("books") MultipartFile file, HttpServletRequest request) {
        System.out.println("Content-Type: " + request.getContentType());
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(400).body("No file uploaded");
            }
            System.out.println("File uploaded: " + file.getOriginalFilename());
            csvDataHandler.processCSVData(file);
            return ResponseEntity.ok("CSV file uploaded, parsed, and processed successfully.");
        } catch (Exception e) {

            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing CSV file: " + e.getMessage());
        }
    }
}
