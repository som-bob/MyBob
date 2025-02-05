package com.my.bob.core.external.s3.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${s3.folder.recipe}")
    private String recipeFolder;

    public FileSaveResponseDto uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String s3FileName = createFileName(originalFilename);
        String contentType = file.getContentType();
        long size = file.getSize();

        try {
            PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(recipeFolder + s3FileName)
                    .contentType(contentType)
                    .build(), RequestBody.fromBytes(file.getBytes()));

            if (response.sdkHttpResponse().isSuccessful()) {
                String fileUrl = getFileUrl(s3FileName);
                return new FileSaveResponseDto(originalFilename, s3FileName, fileUrl, contentType, size);
            } else {
                throw new IllegalArgumentException(ErrorMessage.FILE_ERROR_CONTACT_ADMINISTRATOR);
            }
        } catch (IOException e) {
            log.error("Fail to read file. error message: {}", e.getMessage(), e);
            throw new IllegalArgumentException(ErrorMessage.FILE_ERROR_CONTACT_ADMINISTRATOR);
        } catch (S3Exception e) {
            log.error("AWS s3 service exception. error message: {}", e.getMessage(), e);
            throw new IllegalArgumentException(ErrorMessage.FILE_ERROR_CONTACT_ADMINISTRATOR);
        }
    }

    public void deleteFile(String s3FileName) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(recipeFolder + s3FileName)
                    .build());
        } catch (S3Exception e) {
            log.error("AWS s3 service exception. error message: {}", e.getMessage(), e);
            throw new IllegalArgumentException(ErrorMessage.FILE_ERROR_CONTACT_ADMINISTRATOR);
        }
    }


    private String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s%s", bucket, region, recipeFolder, fileName);
    }

    private String createFileName(String originalFileName) {
        String sanitizedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8);
        return String.format("%s_%s", UUID.randomUUID(), sanitizedFileName);
    }
}
