package org.example.bookstoreproject.service.columnprocessor;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.FileDownloadStatus;
import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookImage;
import org.example.bookstoreproject.persistance.entity.FileMetaData;
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
    private static final String UNIQUE_KEY_DELIMITER = "|||";

    private final ImageUtility imageUtility;
    private final ImageMetaDataRepository imageMetaDataRepository;
    private final BookImageRepository bookImageRepository;

    @Value("${image.processing.enabled}")
    private boolean imageProcessingEnabled;

    public void process(List<CSVRow> data, BookRepository bookRepository) {
        if (!imageProcessingEnabled) {
            return;
        }

        Map<String, Book> bookMap = bookRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(Book::getBookID, book -> book));

        Map<String, FileMetaData> existingMetadataKeys = loadExistingMetadata();
        Map<String, BookImage> existingBookImageKeys = loadExistingBookImages();

        List<FileMetaData> newMetaData = new ArrayList<>();
        List<BookImage> newImages = new ArrayList<>();

        for (CSVRow row : data) {
            if (!shouldProcessRow(row, bookMap)) {
                continue;
            }

            String imageUrl = row.getImage();
            String title = row.getTitle();
            String bookID = row.getBookID();
            Book book = bookMap.get(bookID);

            processImageVariants(imageUrl, title, book, existingMetadataKeys, existingBookImageKeys, newMetaData, newImages);
        }

        saveNewEntities(newMetaData, newImages);
    }

    private boolean shouldProcessRow(CSVRow row, Map<String, Book> bookMap) {
        return row.getImage() != null &&
                !row.getImage().isEmpty() &&
                bookMap.containsKey(row.getBookID()) &&
                imageUtility.isImageUrlValid(row.getImage());
    }

    private void processImageVariants(String imageUrl, String title, Book book,
                                      Map<String, FileMetaData> existingMetadataKeys,
                                      Map<String, BookImage> existingBookImageKeys,
                                      List<FileMetaData> newMetaData,
                                      List<BookImage> newImages) {
        String imagePath = imageUtility.getImagePathForBook(title, BASE_FOLDER);
        List<String> info = getInfo(imagePath);
        String subfolderName = info.get(0);
        String fileName = info.get(1);
        String formatName = info.get(2);

        processImageVariant(imageUrl, book, existingMetadataKeys, existingBookImageKeys,
                newMetaData, newImages, BASE_FOLDER, subfolderName,
                fileName + "_original", formatName, ImageSize.ORIGINAL);

        processImageVariant(imageUrl, book, existingMetadataKeys, existingBookImageKeys,
                newMetaData, newImages, BASE_FOLDER, subfolderName,
                fileName + "_small", formatName, ImageSize.SMALL);

        processImageVariant(imageUrl, book, existingMetadataKeys, existingBookImageKeys,
                newMetaData, newImages, BASE_FOLDER, subfolderName,
                fileName + "_medium", formatName, ImageSize.MEDIUM);
    }

    private void processImageVariant(String imageUrl, Book book,
                                     Map<String, FileMetaData> existingMetadataKeys,
                                     Map<String, BookImage> existingBookImageKeys,
                                     List<FileMetaData> newMetaData,
                                     List<BookImage> newImages,
                                     String mainFolder, String subFolder,
                                     String fileName, String formatName,
                                     ImageSize imageSize) {
        String fileKey = createUniqueKeyFiles(fileName, mainFolder, subFolder);
        String imageKey = createUniqueKeyImages(fileName.replace("_original", "")
                        .replace("_small", "")
                        .replace("_medium", ""),
                book.getBookID(), imageSize);

        if (!existingMetadataKeys.containsKey(fileKey)) {
            FileMetaData meta = createFileMetaData(mainFolder, subFolder, fileName, formatName, imageUrl);
            newMetaData.add(meta);
            existingMetadataKeys.put(fileKey, meta);
        }

        if (!existingBookImageKeys.containsKey(imageKey)) {
            FileMetaData meta = existingMetadataKeys.get(fileKey);
            BookImage bookImage = createBookImage(book, meta, imageSize);
            newImages.add(bookImage);
            existingBookImageKeys.put(imageKey, bookImage);
        }
    }

    private FileMetaData createFileMetaData(String mainFolder, String subFolder,
                                            String fileName, String formatName, String url) {
        FileMetaData meta = new FileMetaData();
        meta.setMainFolderName(mainFolder);
        meta.setSubFolderName(subFolder);
        meta.setFileName(fileName);
        meta.setFormatName(formatName);
        meta.setFileDownloadStatus(FileDownloadStatus.PENDING);
        meta.setUrl(url);
        return meta;
    }

    private BookImage createBookImage(Book book, FileMetaData meta, ImageSize size) {
        BookImage image = new BookImage();
        image.setImageSize(size);
        image.setBook(book);
        image.setImage(meta);
        return image;
    }

    private Map<String, FileMetaData> loadExistingMetadata() {
        return imageMetaDataRepository.findAll().stream()
                .collect(Collectors.toMap(
                        meta -> createUniqueKeyFiles(
                                meta.getFileName(),
                                meta.getMainFolderName(),
                                meta.getSubFolderName()),
                        meta -> meta));
    }

    private Map<String, BookImage> loadExistingBookImages() {
        return bookImageRepository.findAll().stream()
                .collect(Collectors.toMap(
                        meta -> createUniqueKeyImages(
                                meta.getImage().getFileName()
                                        .replace("_original", "")
                                        .replace("_small", "")
                                        .replace("_medium", ""),
                                meta.getBook().getBookID(),
                                meta.getImageSize()),
                        meta -> meta));
    }

    private void saveNewEntities(List<FileMetaData> newMetaData, List<BookImage> newImages) {
        if (!newMetaData.isEmpty()) {
            imageMetaDataRepository.saveAll(newMetaData);
        }
        if (!newImages.isEmpty()) {
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