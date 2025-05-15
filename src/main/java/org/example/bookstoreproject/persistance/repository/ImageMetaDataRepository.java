package org.example.bookstoreproject.persistance.repository;

import org.example.bookstoreproject.persistance.entity.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageMetaDataRepository extends JpaRepository<FileMetaData, Long> {
}