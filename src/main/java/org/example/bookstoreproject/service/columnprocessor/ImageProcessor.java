package org.example.bookstoreproject.service.columnprocessor;

import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ImageUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class ImageProcessor{
    private static final String BASE_FOLDER = "book_images";
    private static final int THUMB_WIDTH = 100;
    private static final int THUMB_HEIGHT = 200;
    private final ImageUtility imageUtility;

    @Value("${image.processing.enabled}")
    private boolean imageProcessingEnabled;

    public ImageProcessor(ImageUtility imageUtility) {
        this.imageUtility = imageUtility;
    }

    public void process(List<CSVRow> data) {
        if (!imageProcessingEnabled) {
            System.out.println("Image processing is disabled. Skipping image processing.");
            return;
        }
        for (CSVRow row : data) {
            String imageUrl = row.getImage();
            String title = row.getTitle();
            if (imageUrl == null || imageUrl.isEmpty()) continue;

            try {
                if (!imageUtility.isImageUrlValid(imageUrl)) continue;

                String imagePath = imageUtility.getImagePathForBook(title, BASE_FOLDER);
                File imageFile = new File(imagePath);
                imageFile.getParentFile().mkdirs();
                imageUtility.downloadImage(imageUrl, imagePath);

                String thumbnailPath = imagePath.replace(".jpg", "_thumb.jpg");
                imageUtility.createThumbnail(imagePath, thumbnailPath, THUMB_WIDTH, THUMB_HEIGHT);

            } catch (Exception e) {
                System.err.println("Failed to process image for book '" + title + "': " + e.getMessage());
            }
        }
    }
}
