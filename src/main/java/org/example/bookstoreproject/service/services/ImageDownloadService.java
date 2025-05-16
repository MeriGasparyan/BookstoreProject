package org.example.bookstoreproject.service.services;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.FileDownloadStatus;
import org.example.bookstoreproject.persistance.entity.FileMetaData;
import org.example.bookstoreproject.persistance.repository.ImageMetaDataRepository;
import org.example.bookstoreproject.service.utility.ImageUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageDownloadService {
    private final ImageMetaDataRepository imageMetaDataRepository;
    private final ImageUtility imageUtility;
    private static final int SMALL_WIDTH = 80;
    private static final int SMALL_HEIGHT = 120;
    private static final int MEDIUM_WIDTH = 200;
    private static final int MEDIUM_HEIGHT = 300;

    @Value("${image.download.batch.size}")
    private int batchSize;

    @Transactional
    public void processPendingDownloads() {
        List<FileMetaData> pendingDownloads = imageMetaDataRepository
                .findByFileDownloadStatus(FileDownloadStatus.PENDING, PageRequest.of(0, batchSize));

        for (FileMetaData meta : pendingDownloads) {
            try {
                String path = buildImagePath(meta);
                new File(path).getParentFile().mkdirs();

                if (path.endsWith("_original.jpg")) {
                    imageUtility.downloadImage(meta.getUrl(), path);
                } else {
                    String originalPath = path.replace("_small.jpg", "_original.jpg")
                            .replace("_medium.jpg", "_original.jpg");
                    if (path.endsWith("_small.jpg")) {
                        imageUtility.createThumbnail(originalPath, path, SMALL_WIDTH, SMALL_HEIGHT);
                    } else {
                        imageUtility.createThumbnail(originalPath, path, MEDIUM_WIDTH, MEDIUM_HEIGHT);
                    }
                }
                meta.setFileDownloadStatus(FileDownloadStatus.COMPLETED);
            } catch (Exception e) {
                meta.setFileDownloadStatus(FileDownloadStatus.FAILED);
            }
            imageMetaDataRepository.save(meta);
        }
    }

    private String buildImagePath(FileMetaData meta) {
        return meta.getMainFolderName() + File.separator +
                meta.getSubFolderName() + File.separator +
                meta.getFileName() + "." + meta.getFormatName();
    }
}