package com.my.bob.core.external.s3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@Slf4j
@SpringBootTest
class S3ServiceTest {
    
    @Autowired
    private S3Service s3Service;

    @Test
    @DisplayName("S3 버킷 저장 테스트")
    void putObject_success() throws IOException {
        // given
        // resource 파일 읽어 와서 적용

        // when
        
        // then
    }

}