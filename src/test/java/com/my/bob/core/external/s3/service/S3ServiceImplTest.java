package com.my.bob.core.external.s3.service;

import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("S3 서비스 테스트")
class S3ServiceImplTest {

    @Autowired
    private S3Service s3Service;

    private String uploadFileName;

    @AfterEach
    void tearDown() {
        if(StringUtils.isNotBlank(uploadFileName)) {
            assertDoesNotThrow(() -> s3Service.deleteFile(uploadFileName));
        }
    }

    @Test
    @DisplayName("S3 버킷 저장 테스트")
    void uploadFile() throws IOException {
        // given
        // resource 파일 읽어 와서 적용
        ClassPathResource resource = new ClassPathResource("file/test.png");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                resource.getInputStream());

        // when
        FileSaveResponseDto fileSaveResponseDto = s3Service.uploadFile(multipartFile);

        // then
        assertThat(fileSaveResponseDto).isNotNull();
        assertThat(fileSaveResponseDto.getOriginalFilename()).isEqualTo("test.png");
        assertThat(fileSaveResponseDto.getFileName()).isNotBlank();
        assertThat(fileSaveResponseDto.getFileUrl()).isNotBlank();
        assertThat(fileSaveResponseDto.getFileSize()).isPositive();

        this.uploadFileName = fileSaveResponseDto.getFileName();
        log.info("file Url: {}", fileSaveResponseDto.getFileUrl());
    }
}