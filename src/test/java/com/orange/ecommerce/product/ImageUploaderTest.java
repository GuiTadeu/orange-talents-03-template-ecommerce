package com.orange.ecommerce.product;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageUploaderTest {

    @Test
    public void upload__should_return_image_url() {

        var image = new MockMultipartFile(
                "image",
                "image01.png",
                MediaType.IMAGE_PNG_VALUE,
                "image".getBytes());

        String imageUrl = ImageUploader.upload(image);

        String originalFilename = image.getOriginalFilename();
        String imageExtension = getExtension(originalFilename);
        String imageName = removeExtension(originalFilename);

        assertTrue(imageUrl.matches(String.format("^/%s-[0-9]+.%s", imageName, imageExtension)));
    }
}