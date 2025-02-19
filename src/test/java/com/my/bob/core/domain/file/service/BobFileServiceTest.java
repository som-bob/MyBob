package com.my.bob.core.domain.file.service;

import com.my.bob.core.domain.file.constant.FileRoute;
import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.file.repsitory.BobFileRepository;
import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import com.my.bob.core.external.s3.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("파일 저장, 삭제 테스트(S3 저장, 삭제 테스트 까지 함께 진행)")
class BobFileServiceTest {

    @Autowired
    private BobFileService bobFileService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private BobFileRepository bobFileRepository;

    private static Long saveFileId;
        
    @Test
    @Order(0)
    @DisplayName("파일 저장 + S3 파일 저장 테스트")
    void newFile_Success() throws IOException {
        // given
        // resource 파일 읽어 와서 S3에 저장
        ClassPathResource resource = new ClassPathResource("file/test.png");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                resource.getInputStream());
        FileSaveResponseDto saveResponseDto = s3Service.uploadFile(multipartFile);
        log.info("fileUrl: {}", saveResponseDto.getFileUrl());

        // when
        BobFile bobFile = bobFileService.newFile(saveResponseDto.getFileUrl(),
                saveResponseDto.getOriginalFilename(),
                saveResponseDto.getFileName(),
                saveResponseDto.getFileSize(),
                saveResponseDto.getContentType(),
                FileRoute.TEST);
        saveFileId = bobFile.getFileId();

        // then
        assertThat(saveFileId).isPositive();
        assertThat(bobFile.getIsDeleted()).isFalse();
        assertThat(bobFile.getRoute()).isEqualTo(FileRoute.TEST);
    }

    @Test
    @Order(1)
    @DisplayName("파일 삭제 + 실제 S3 파일 삭제 테스트")
    void deleteFile_Success() {
        // given
        assertThat(saveFileId).isNotNull();
        BobFile bobFile = getBobFile();
        String fileName = bobFile.getFileName();

        // when
        s3Service.deleteFile(fileName);

        // then
        bobFileService.deleteFile(saveFileId);
        BobFile refindFile = getBobFile();
        assertThat(refindFile.getIsDeleted()).isTrue();
    }

    private BobFile getBobFile() {
        Optional<BobFile> bobFileOptional = bobFileRepository.findById(saveFileId);
        if(bobFileOptional.isEmpty()) {
            fail("fail to find bob file");
        }
        return bobFileOptional.get();
    }

}
