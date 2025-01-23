package com.my.bob.core.external.s3.service;

import com.my.bob.core.constants.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String RECIPE_FILE_FOLDER = "recipe/";

    public void putObject(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(RECIPE_FILE_FOLDER + originalFilename)
                .contentType(contentType)
                .build();

        PutObjectResponse response;
        try {
            response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            log.error("Fail to read file. error message: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

        if(response.sdkHttpResponse().isSuccessful()) {
            log.info("{}", response); // TODO 테스트 후에 S3 url을 전달하도록 변경
        } else {
            throw new IllegalArgumentException(ErrorMessage.FILE_ERROR_CONTACT_ADMINISTRATOR);
        }
    }
}
