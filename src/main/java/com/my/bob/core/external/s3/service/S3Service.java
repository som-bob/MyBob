package com.my.bob.core.external.s3.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${s3.folder.recipe}")
    private String recipeFolder;

    public FileSaveResponseDto putRecipeObject(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileName = createFileName(originalFilename);
        String contentType = file.getContentType();

        try {
            PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(recipeFolder + fileName)
                    .contentType(contentType)
                    .build(), RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful()) {
                String fileUrl = getFileUrl(fileName);
                return new FileSaveResponseDto(originalFilename, contentType, fileName, fileUrl);
            } else {
                throw new IllegalArgumentException(ErrorMessage.FILE_ERROR_CONTACT_ADMINISTRATOR);
            }
        } catch (IOException e) {
            log.error("Fail to read file. error message: {}", e.getMessage(), e);
            throw new IllegalArgumentException(ErrorMessage.FILE_ERROR_CONTACT_ADMINISTRATOR);
        } catch (AwsServiceException e) {
            log.error("AWS service exception. error message: {}", e.getMessage(), e);
            throw new IllegalArgumentException(ErrorMessage.FILE_ERROR_CONTACT_ADMINISTRATOR);
        }
    }

    public void deleteRecipeObject(String fileName) {

    }


    private String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", region, bucket, fileName);
    }

    private String createFileName(String originalFileName) {
        return String.format("%s_%s", UUID.randomUUID(), originalFileName);
    }
}
