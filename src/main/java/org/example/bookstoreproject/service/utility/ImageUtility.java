package org.example.bookstoreproject.service.utility;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUtility {

    public boolean isImageUrlValid(String urlStr) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    public void downloadImage(String urlStr, String destinationPath) throws IOException {
        try (InputStream in = new URL(urlStr).openStream()) {
            Files.copy(in, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public String getImagePathForBook(String title, String baseFolder) {
        String safeTitle = title.replaceAll("[^a-zA-Z0-9]", "_");
        String subfolder = safeTitle.isEmpty() ? "unknown" : safeTitle.substring(0, 1).toLowerCase();
        return baseFolder + File.separator + subfolder + File.separator + safeTitle + ".jpg";
    }

    public void createThumbnail(String originalPath, String thumbPath, int width, int height) throws IOException {
        BufferedImage img = ImageIO.read(new File(originalPath));
        BufferedImage thumbnail = Thumbnails.of(img)
                .size(width, height)
                .asBufferedImage();

        File thumbFile = new File(thumbPath);
        thumbFile.getParentFile().mkdirs(); // Just in case
        ImageIO.write(thumbnail, "jpg", thumbFile);
    }
}
