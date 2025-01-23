package com.my.bob.core.external.s3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

@Slf4j
@SpringBootTest
class S3ServiceTest {
    
    @Autowired
    private S3Service s3Service;


    // TODO 작성하기
    @Test
    @DisplayName("S3 버킷 저장 테스트")
    void putObject_success() throws IOException {
        // given
        ClassPathResource classPathResource = new ClassPathResource("test.png");
        File file = classPathResource.getFile();



        // when
        
        // then
    }

}