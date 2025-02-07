package com.my.bob.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ResourceUtil {

    private ResourceUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static MultipartFile getFileFromResource(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("file/" + fileName);
        return new MockMultipartFile(
                "file",
                fileName,
                "image/png",
                resource.getInputStream());
    }
}
