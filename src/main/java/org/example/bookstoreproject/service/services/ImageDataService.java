package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entity.Book;
import org.example.bookstoreproject.persistance.entity.BookImage;
import org.example.bookstoreproject.persistance.entity.FileMetaData;
import org.example.bookstoreproject.persistance.repository.BookImageRepository;
import org.example.bookstoreproject.persistance.repository.BookRepository;
import org.example.bookstoreproject.persistance.repository.ImageMetaDataRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageDataService {

    private final BookImageRepository bookImageRepository;

    public InputStreamResource getImage(Long bookId, ImageSize size) throws IOException {
        BookImage bookImage = bookImageRepository.findByBookAndImageSize(bookId, size).orElseThrow();
        FileMetaData image = bookImage.getImage();
        System.out.println(image.getFileName());
        String imagePath = this.buildImagePath(image.getMainFolderName(), image.getSubFolderName(),
                image.getFileName(), image.getFormatName());
        if (imagePath == null) {
            throw new NoSuchElementException("Image not found");
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new NoSuchElementException("Image not found");
        }

        InputStream inputStream = new FileInputStream(imageFile);
        return new InputStreamResource(inputStream);

    }

    private String buildImagePath(String mainFolder, String subFolder, String fileName, String format) {
        return mainFolder + "/" + subFolder + "/" + fileName + "." + format;
    }
}
