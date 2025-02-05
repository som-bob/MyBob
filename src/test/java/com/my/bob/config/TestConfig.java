package com.my.bob.config;

import com.my.bob.core.external.s3.service.S3Service;
import com.my.bob.stub.S3StubService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    /*
     * 테스트 코드 실행시에만 적용된다.
     * 따라서
     */

    @Bean
    public S3Service s3Service() {
        return new S3StubService();
    }
}
