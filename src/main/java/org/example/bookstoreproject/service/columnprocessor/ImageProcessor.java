package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.ImageMetaData;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.ImageMetaDataRepository;
import org.example.bookstoreproject.service.CSVRow;
import org.example.bookstoreproject.service.utility.ImageUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageProcessor {
    private static final String BASE_FOLDER = "book_images";
    private static final int SMALL_WIDTH = 80;
    private static final int SMALL_HEIGHT = 120;
    private static final int MEDIUM_WIDTH = 200;
    private static final int MEDIUM_HEIGHT = 300;
    private static final String UNIQUE_KEY_DELIMITER = "|||";

    private final ImageUtility imageUtility;
    private final ImageMetaDataRepository imageMetaDataRepository;

    @Value("${image.processing.enabled}")
    private boolean imageProcessingEnabled;

    public void process(List<CSVRow> data, BookRepository bookRepository) {
        if (!imageProcessingEnabled) {
            System.out.println("Image processing is disabled.");
            return;
        }


        Map<String, Book> bookMap = bookRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Book::getBookID, book -> book));

        Set<String> existingMetadataKeys = imageMetaDataRepository.findAll().stream()
                .map(meta -> createUniqueKey(
                        meta.getFileName(),
                        meta.getBook().getBookID(),
                        meta.getImageSize()))
                .collect(Collectors.toSet());

        List<ImageMetaData> newMetaData = new ArrayList<>();

        for (CSVRow row : data) { String imageUrl = row.getImage();
            String title = row.getTitle();
            String bookID = row.getBookID();
            Book book = bookMap.get(bookID);

            if (book == null || imageUrl == null || imageUrl.isEmpty() || !imageUtility.isImageUrlValid(imageUrl)) {
                return;
            }

            try {

                String imagePath = imageUtility.getImagePathForBook(title, BASE_FOLDER);
                File imageFile = new File(imagePath);
                imageFile.getParentFile().mkdirs();
                imageUtility.downloadImage(imageUrl, imagePath);

                List<String> info = getInfo(imagePath);
                String fileName = info.get(1);
                String formatName = info.get(2);

                String uniqueKeySmall = createUniqueKey(fileName, bookID, ImageSize.SMALL);
                String uniqueKeyMedium = createUniqueKey(fileName, bookID, ImageSize.MEDIUM);
                String uniqueKeyOriginal = createUniqueKey(fileName, bookID, ImageSize.ORIGINAL);

                String smallPath = imagePath.replace(".jpg", "_small.jpg");
                String mediumPath = imagePath.replace(".jpg", "_medium.jpg");
                imageUtility.createThumbnail(imagePath, smallPath, SMALL_WIDTH, SMALL_HEIGHT);
                imageUtility.createThumbnail(imagePath, mediumPath, MEDIUM_WIDTH, MEDIUM_HEIGHT);

                if (!existingMetadataKeys.contains(uniqueKeySmall)) {
                    ImageMetaData imageMetaDataSmall = new ImageMetaData();
                    imageMetaDataSmall.setMainFolderName(BASE_FOLDER);
                    imageMetaDataSmall.setSubFolderName(info.get(0));
                    imageMetaDataSmall.setFileName(fileName);
                    imageMetaDataSmall.setFormatName(formatName);
                    imageMetaDataSmall.setBook(book);
                    imageMetaDataSmall.setImageSize(ImageSize.SMALL);
                    existingMetadataKeys.add(uniqueKeySmall);
                    newMetaData.add(imageMetaDataSmall);
                }
                if(!existingMetadataKeys.contains(uniqueKeyMedium)) {
                    ImageMetaData imageMetaDataMedium = new ImageMetaData();
                    imageMetaDataMedium.setMainFolderName(BASE_FOLDER);
                    imageMetaDataMedium.setSubFolderName(info.get(0));
                    imageMetaDataMedium.setFileName(fileName);
                    imageMetaDataMedium.setFormatName(formatName);
                    imageMetaDataMedium.setBook(book);
                    imageMetaDataMedium.setImageSize(ImageSize.MEDIUM);
                    existingMetadataKeys.add(uniqueKeyMedium);
                    newMetaData.add(imageMetaDataMedium);
                }

                if (!existingMetadataKeys.contains(uniqueKeyOriginal)) {
                    ImageMetaData imageMetaDataOriginal = new ImageMetaData();
                    imageMetaDataOriginal.setMainFolderName(BASE_FOLDER);
                    imageMetaDataOriginal.setSubFolderName(info.get(0));
                    imageMetaDataOriginal.setFileName(fileName);
                    imageMetaDataOriginal.setFormatName(formatName);
                    imageMetaDataOriginal.setBook(book);
                    imageMetaDataOriginal.setImageSize(ImageSize.ORIGINAL);
                    existingMetadataKeys.add(uniqueKeyOriginal);
                    newMetaData.add(imageMetaDataOriginal);
                }
            } catch (Exception e) {
                System.err.println("Failed to process image for book '" + title + "': " + e.getMessage());
            }

        }
        if (!newMetaData.isEmpty()) {
            imageMetaDataRepository.saveAll(newMetaData);
        }


    }

    private List<String> getInfo(String imageUrl) {
        String[] folders = imageUrl.split("/");
        List<String> paths = new ArrayList<>(folders.length + 1);

        if (folders.length > 2) {
            Collections.addAll(paths, Arrays.copyOfRange(folders, 1, folders.length - 1));
        }

        String[] fileParts = folders[folders.length - 1].split("\\.");
        Collections.addAll(paths, fileParts);

        return paths;
    }

    private String createUniqueKey(String fileName, String bookId, ImageSize imageSize) {
        return fileName + UNIQUE_KEY_DELIMITER + bookId + UNIQUE_KEY_DELIMITER + imageSize.name();
    }
}