package com.orange.ecommerce.product;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class ImageUploader {

    public static String upload(MultipartFile image) {
        return generateImageUrl(image.getOriginalFilename());
    }

    /**
     * @@implNote - Example of return: /image01-04562.png
     * */
    private static String generateImageUrl(String originalFilename) {
        String imageExtension = FilenameUtils.getExtension(originalFilename);
        String imageName = FilenameUtils.removeExtension(originalFilename);

        Calendar instance = Calendar.getInstance();
        Date date = instance.getTime();

        return String.format("/%s%d.%s", imageName, date.hashCode(), imageExtension);
    }
}
