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
        for (CSVRow row : data) {
            String imageUrl = row.getImage();
            String title = row.getTitle();
            String bookID = row.getBookID();
            Book book = bookMap.get(bookID);

            if (book == null || imageUrl == null || imageUrl.isEmpty() || !imageUtility.isImageUrlValid(imageUrl)) {
                continue;
            }
                String imagePath = imageUtility.getImagePathForBook(title, BASE_FOLDER);
                List<String> info = getInfo(imagePath);
                String fileName = info.get(1);
                String formatName = info.get(2);
                String subfolderName = info.get(0);

                String fileNameSmall = fileName + "_small";
                String fileNameMedium = fileName + "_medium";

                String uniqueKeyFile = createUniqueKeyFiles(fileName, BASE_FOLDER, subfolderName);
                String uniqueKeySmallImageFile = createUniqueKeyFiles(fileNameSmall, BASE_FOLDER, subfolderName);
                String uniqueKeyMediumImageFile = createUniqueKeyFiles(fileNameMedium, BASE_FOLDER, subfolderName);
                String uniqueKeySmall = createUniqueKeyImages(fileName, bookID, ImageSize.SMALL);
                String uniqueKeyMedium = createUniqueKeyImages(fileName, bookID, ImageSize.MEDIUM);
                String uniqueKeyOriginal = createUniqueKeyImages(fileName, bookID, ImageSize.ORIGINAL);


                FileMetaData fileMetaData = new FileMetaData();
                FileMetaData fileMetaDataSmall = new FileMetaData();
                FileMetaData fileMetaDataMedium = new FileMetaData();

                if(existingMetadataKeys.get(uniqueKeyFile) == null) {

                    fileMetaData.setMainFolderName(BASE_FOLDER);
                    fileMetaData.setSubFolderName(subfolderName);
                    fileMetaData.setFileName(fileName + "_original");
                    fileMetaData.setFormatName(formatName);
                    fileMetaData.setFileDownloadStatus(FileDownloadStatus.PENDING);
                    fileMetaData.setUrl(imageUrl);
                    newMetaData.add(fileMetaData);
                    existingMetadataKeys.put(uniqueKeyFile, fileMetaData);
                }
                if(existingBookImageKeys.get(uniqueKeySmallImageFile) == null) {

                    fileMetaDataSmall.setMainFolderName(BASE_FOLDER);
                    fileMetaDataSmall.setSubFolderName(subfolderName);
                    fileMetaDataSmall.setFileName(fileNameSmall);
                    fileMetaDataSmall.setFormatName(formatName);
                    fileMetaDataSmall.setFileDownloadStatus(FileDownloadStatus.PENDING);
                    fileMetaDataSmall.setUrl(imageUrl);
                    newMetaData.add(fileMetaDataSmall);
                    existingMetadataKeys.put(uniqueKeySmallImageFile, fileMetaDataSmall);
                }
                if(existingMetadataKeys.get(uniqueKeyMediumImageFile) == null) {

                    fileMetaDataMedium.setMainFolderName(BASE_FOLDER);
                    fileMetaDataMedium.setSubFolderName(subfolderName);
                    fileMetaDataMedium.setFileName(fileNameMedium);
                    fileMetaDataMedium.setFormatName(formatName);
                    fileMetaDataMedium.setFileDownloadStatus(FileDownloadStatus.PENDING);
                    fileMetaDataMedium.setUrl(imageUrl);
                    newMetaData.add(fileMetaDataMedium);
                    existingMetadataKeys.put(uniqueKeyMediumImageFile, fileMetaDataMedium);
                }
                if (existingBookImageKeys.get(uniqueKeySmall) == null) {
                    BookImage bookImageSmall = new BookImage();
                    bookImageSmall.setImageSize(ImageSize.SMALL);
                    bookImageSmall.setBook(book);
                    bookImageSmall.setImage(fileMetaDataSmall);
                    existingBookImageKeys.put(uniqueKeySmall, bookImageSmall);
                    newImages.add(bookImageSmall);
                }
                if(existingBookImageKeys.get(uniqueKeyMedium) == null) {
                    BookImage bookImageMedium = new BookImage();
                    bookImageMedium.setImageSize(ImageSize.MEDIUM);
                    bookImageMedium.setBook(book);
                    bookImageMedium.setImage(fileMetaDataMedium);
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
        }

        for (FileMetaData fileMetaDataEntry : existingMetadataKeys.values()) {
            if (fileMetaDataEntry.getFileDownloadStatus().equals(FileDownloadStatus.PENDING)) {
                String path =fileMetaDataEntry.getMainFolderName() + File.separator
                        + fileMetaDataEntry.getSubFolderName() + File.separator +
                        fileMetaDataEntry.getFileName() + "." + fileMetaDataEntry.getFormatName();

                if (path.endsWith("_original.jpg")) {
                    try {
                        File imageFile = new File(path);
                        imageFile.getParentFile().mkdirs();
                        imageUtility.downloadImage(fileMetaDataEntry.getUrl(), path);
                        fileMetaDataEntry.setFileDownloadStatus(FileDownloadStatus.COMPLETED);
                    } catch (Exception e) {
                        fileMetaDataEntry.setFileDownloadStatus(FileDownloadStatus.FAILED);
                        System.out.println("Original image download failed: " + e.getMessage());
                    }
                }
            }
        }
        for (FileMetaData fileMetaDataEntry : existingMetadataKeys.values()) {
            if (fileMetaDataEntry.getFileDownloadStatus().equals(FileDownloadStatus.PENDING)) {
                String path = fileMetaDataEntry.getMainFolderName() + File.separator
                        + fileMetaDataEntry.getSubFolderName() + File.separator +
                        fileMetaDataEntry.getFileName() + "." + fileMetaDataEntry.getFormatName();

                try {
                    String imagePath;
                    if (path.endsWith("_small.jpg")) {
                        imagePath = path.replace("_small.jpg", "_original.jpg");
                        imageUtility.createThumbnail(imagePath, path, SMALL_WIDTH, SMALL_HEIGHT);
                    } else if (path.endsWith("_medium.jpg")) {
                        imagePath = path.replace("_medium.jpg", "_original.jpg");
                        imageUtility.createThumbnail(imagePath, path, MEDIUM_WIDTH, MEDIUM_HEIGHT);
                    } else {
                        continue;
                    }

                    fileMetaDataEntry.setFileDownloadStatus(FileDownloadStatus.COMPLETED);
                } catch (Exception e) {
                    fileMetaDataEntry.setFileDownloadStatus(FileDownloadStatus.FAILED);
                    System.out.println("Thumbnail creation failed: " + e.getMessage());
                }
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