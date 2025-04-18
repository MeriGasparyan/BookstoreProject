package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.persistance.entry.ImageMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageMetaDataRepository extends JpaRepository<ImageMetaData, Long> {
    Optional<ImageMetaData> findByBook_IdAndImageSize(Long bookId, ImageSize imageSize);
}