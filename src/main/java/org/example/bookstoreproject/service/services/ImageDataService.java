package org.example.bookstoreproject.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class ImageDataService {

    private static final String METADATA_FILE = "book_images/image_metadata.json";
    private Map<String, Map<String, String>> imageMetadata;

    public ImageDataService() {
        loadMetadata();
    }

    private void loadMetadata() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(METADATA_FILE);
            if (file.exists()) {
                imageMetadata = mapper.readValue(file, Map.class);
            } else {
                imageMetadata = Map.of();
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageMetadata = Map.of();
        }
    }

    public String getImagePath(String bookId, String size) {
        if (imageMetadata.containsKey(bookId)) {
            return imageMetadata.get(bookId).get(size);
        }
        return null;
    }
}
