package com.my.bob.core.external.s3.service;

import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    FileSaveResponseDto uploadFile(MultipartFile file);
    void deleteFile(String s3FileName);
}
