package com.my.bob.config;

import com.my.bob.core.external.s3.service.S3Service;
import com.my.bob.stub.S3StubService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import redis.embedded.RedisServer;

@TestConfiguration
public class TestConfig {
    /*
     * 테스트 코드 실행시에만 적용된다.
     * 그러니, 실제 외부 연동되어야 하는 서비스의 경우 stub 생성
     */

    private static RedisServer redisServer;

    @Bean
    public S3Service s3Service() {
        return new S3StubService();
    }

    @PostConstruct
    public void startRedisServer() {
        if(redisServer == null || ! redisServer.isActive()) {
            redisServer = new RedisServer(6380);
            redisServer.start();
        }
    }

    @PreDestroy
    public void stopRedisServer() {
        if(redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }

    @Bean
    public RedisServer redisServer() {
        return redisServer;
    }
}
