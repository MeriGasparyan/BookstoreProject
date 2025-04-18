package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.enums.ImageSize;
import org.example.bookstoreproject.service.services.ImageDataService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class ImageController {

    private final ImageDataService metadataService;

    @GetMapping("/{id}/image")
    public ResponseEntity<InputStreamResource> getImage(
            @PathVariable("id") Long bookId,
            @RequestParam(value = "size", defaultValue = "original") String size
    ) {
        String imagePath = metadataService.getImagePath(bookId, ImageSize.fromString(size));
        if (imagePath == null) {
            System.out.println(11111111);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.out.println(22222);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            InputStream inputStream = new FileInputStream(imageFile);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
