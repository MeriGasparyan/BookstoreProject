package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entry.Book;
import org.example.bookstoreproject.persistance.entry.BookImage;
import org.example.bookstoreproject.persistance.entry.FileMetaData;
import org.example.bookstoreproject.persistance.repository.BookImageRepository;
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
    private final BookImageRepository bookImageRepository;

    @Value("${image.processing.enabled}")
    private boolean imageProcessingEnabled;

    public void process(List<CSVRow> data, BookRepository bookRepository) {
        if (!imageProcessingEnabled) {
            System.out.println("Image processing is disabled.");
            return;
        }

        Map<String, Book> bookMap = bookRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Book::getBookID, book -> book));

        Map<String, FileMetaData> existingMetadataKeys = imageMetaDataRepository.findAll().stream()
                .collect(Collectors.toMap(
                        meta -> createUniqueKeyFiles(
                                meta.getFileName(),
                                meta.getMainFolderName(),
                                meta.getSubFolderName()),
                        meta -> meta
                ));

        Map<String, BookImage> existingBookImageKeys = bookImageRepository.findAll().stream()
                .collect(Collectors.toMap(meta -> createUniqueKeyImages(
                        meta.getImage().getFileName(),
                        meta.getBook().getBookID(),
                        meta.getImageSize()),
                        meta -> meta));

        List<FileMetaData> newMetaData = new ArrayList<>();
        List<BookImage> newImages = new ArrayList<>();
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
                String subfolderName = info.get(0);

                String uniqueKeyFile = createUniqueKeyFiles(fileName, BASE_FOLDER, subfolderName);

                String uniqueKeySmall = createUniqueKeyImages(fileName, bookID, ImageSize.SMALL);
                String uniqueKeyMedium = createUniqueKeyImages(fileName, bookID, ImageSize.MEDIUM);
                String uniqueKeyOriginal = createUniqueKeyImages(fileName, bookID, ImageSize.ORIGINAL);

                String smallPath = imagePath.replace(".jpg", "_small.jpg");
                String mediumPath = imagePath.replace(".jpg", "_medium.jpg");
                imageUtility.createThumbnail(imagePath, smallPath, SMALL_WIDTH, SMALL_HEIGHT);
                imageUtility.createThumbnail(imagePath, mediumPath, MEDIUM_WIDTH, MEDIUM_HEIGHT);

                FileMetaData fileMetaData = new FileMetaData();
                if(existingMetadataKeys.get(uniqueKeyFile) == null) {
                    fileMetaData.setMainFolderName(BASE_FOLDER);
                    fileMetaData.setSubFolderName(info.get(0));
                    fileMetaData.setFileName(fileName);
                    fileMetaData.setFormatName(formatName);
                    newMetaData.add(fileMetaData);
                    existingMetadataKeys.put(uniqueKeyFile, fileMetaData);
                }
                else{
                    fileMetaData = existingMetadataKeys.get(uniqueKeyFile);
                }

                if (existingBookImageKeys.get(uniqueKeySmall) == null) {
                    BookImage bookImageSmall = new BookImage();
                    bookImageSmall.setImageSize(ImageSize.SMALL);
                    bookImageSmall.setBook(book);
                    bookImageSmall.setImage(fileMetaData);
                    existingBookImageKeys.put(uniqueKeySmall, bookImageSmall);
                    newImages.add(bookImageSmall);
                }
                if(existingBookImageKeys.get(uniqueKeyMedium) == null) {
                    BookImage bookImageMedium = new BookImage();
                    bookImageMedium.setImageSize(ImageSize.MEDIUM);
                    bookImageMedium.setBook(book);
                    bookImageMedium.setImage(fileMetaData);
                    existingBookImageKeys.put(uniqueKeyMedium, bookImageMedium);
                    newImages.add(bookImageMedium);
                }

                if (existingBookImageKeys.get(uniqueKeyOriginal) == null) {
                    BookImage bookImageOriginal = new BookImage();
                    bookImageOriginal.setImageSize(ImageSize.ORIGINAL);
                    bookImageOriginal.setBook(book);
                    bookImageOriginal.setImage(fileMetaData);
                    existingBookImageKeys.put(uniqueKeyOriginal, bookImageOriginal);
                    newImages.add(bookImageOriginal);
                }
            } catch (Exception e) {
                System.err.println("Failed to process image for book '" + title + "': " + e.getMessage());
            }

        }
        if (!newMetaData.isEmpty()) {
            System.out.println(111);
            imageMetaDataRepository.saveAll(newMetaData);
        }
        if (!newImages.isEmpty()) {
            System.out.println(222);
            bookImageRepository.saveAll(newImages);
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

    private String createUniqueKeyFiles(String fileName, String mainFolder, String subFolder) {
        return mainFolder + UNIQUE_KEY_DELIMITER + subFolder + UNIQUE_KEY_DELIMITER + fileName;
    }

    private String createUniqueKeyImages(String fileName, String bookId, ImageSize size) {
        return fileName + UNIQUE_KEY_DELIMITER + bookId + UNIQUE_KEY_DELIMITER + size.name();
    }
}