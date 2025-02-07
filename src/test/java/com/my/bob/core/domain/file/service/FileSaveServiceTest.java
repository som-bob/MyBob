package com.my.bob.core.domain.file.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("S3, file DB 저장 하는 서비스 테스트")
@WithAccount("system@system.com")
class FileSaveServiceTest {

    @Autowired
    private FileSaveService fileSaveService;
    
    @Test
    @DisplayName("파일 저장 테스트 - 성공")
    void uploadAndSaveFile_success() throws IOException {
        // given
        MultipartFile file = ResourceUtil.getFileFromResource("test.png");

        // when
        BobFile bobFile = fileSaveService.uploadAndSaveFile(file);

        // then
        assertThat(bobFile).isNotNull();
        assertThat(bobFile.getFileId()).isPositive();
        assertThat(bobFile.getFileUrl()).isNotBlank();
        assertThat(bobFile.getOriginalFileName()).isEqualTo("test.png");
    }
    
    
}
