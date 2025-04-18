package org.example.bookstoreproject.service.columnprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ImageUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ImageProcessor {
    private static final String BASE_FOLDER = "book_images";
    private static final int SMALL_WIDTH = 80;
    private static final int SMALL_HEIGHT = 120;
    private static final int MEDIUM_WIDTH = 200;
    private static final int MEDIUM_HEIGHT = 300;

    private final ImageUtility imageUtility;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // 10 threads
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${image.processing.enabled}")
    private boolean imageProcessingEnabled;

    public ImageProcessor(ImageUtility imageUtility) {
        this.imageUtility = imageUtility;
    }

    public void process(List<CSVRow> data) {
        if (!imageProcessingEnabled) {
            System.out.println("Image processing is disabled.");
            return;
        }

        Map<String, Map<String, String>> imageInfoMap = Collections.synchronizedMap(new HashMap<>());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (CSVRow row : data) {
            futures.add(CompletableFuture.runAsync(() -> {
                String imageUrl = row.getImage();
                String title = row.getTitle();
                String bookID = row.getBookID();

                if (imageUrl == null || imageUrl.isEmpty() || !imageUtility.isImageUrlValid(imageUrl)) return;

                try {
                    String imagePath = imageUtility.getImagePathForBook(title, BASE_FOLDER);
                    File imageFile = new File(imagePath);
                    imageFile.getParentFile().mkdirs();
                    imageUtility.downloadImage(imageUrl, imagePath);

                    String smallPath = imagePath.replace(".jpg", "_small.jpg");
                    String mediumPath = imagePath.replace(".jpg", "_medium.jpg");

                    imageUtility.createThumbnail(imagePath, smallPath, SMALL_WIDTH, SMALL_HEIGHT);
                    imageUtility.createThumbnail(imagePath, mediumPath, MEDIUM_WIDTH, MEDIUM_HEIGHT);

                    Map<String, String> paths = new HashMap<>();
                    paths.put("original", imagePath);
                    paths.put("small", smallPath);
                    paths.put("medium", mediumPath);

                    imageInfoMap.put(bookID, paths);

                    System.out.println("Processed images for: " + title);
                } catch (Exception e) {
                    System.err.println("Failed to process image for book '" + title + "': " + e.getMessage());
                }
            }, executorService));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        try (FileWriter writer = new FileWriter(BASE_FOLDER + "/image_metadata.json")) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, imageInfoMap);
            System.out.println("Saved image metadata to JSON.");
        } catch (Exception e) {
            System.err.println("Failed to write metadata JSON: " + e.getMessage());
        }
    }
}
