package com.my.bob.config;

import com.my.bob.core.external.redis.service.RedisService;
import com.my.bob.core.external.s3.service.S3Service;
import com.my.bob.stub.RedisStubService;
import com.my.bob.stub.S3StubService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    /*
     * 테스트 코드 실행시에만 적용된다.
     * 그러니, 실제 외부 연동되어야 하는 서비스의 경우 stub 생성
     */

    @Bean
    public S3Service s3Service() {
        return new S3StubService();
    }

    @Bean
    public RedisService redisService() {
        return new RedisStubService();
    }

}
