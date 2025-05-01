package org.example.bookstoreproject.service.services;

import org.example.bookstoreproject.persistance.repository.ImageMetaDataRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageDataService {

    private final ImageMetaDataRepository imageMetaDataRepository;

    public ImageDataService(ImageMetaDataRepository imageMetaDataRepository) {
        this.imageMetaDataRepository = imageMetaDataRepository;
    }

//    public String getImagePath(Long bookId, ImageSize size) {
//        Optional<FileMetaData> imageMetaData = imageMetaDataRepository.findByBook_IdAndImageSize(bookId, size);
//        System.out.println(imageMetaData.get().getFileName());
//        return imageMetaData
//                .map(meta -> buildImagePath(meta.getMainFolderName(), meta.getSubFolderName(), meta.getFileName(), meta.getFormatName()))
//                .orElse(null);
//    }

    private String buildImagePath(String mainFolder, String subFolder, String fileName, String format) {
        return mainFolder + "/" + subFolder + "/" + fileName + "." + format;
    }
}
