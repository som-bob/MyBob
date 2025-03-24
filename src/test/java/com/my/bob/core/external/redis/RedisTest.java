package com.my.bob.core.external.redis;

import com.my.bob.core.external.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Redis 서비스 테스트")
class RedisTest {

    @Autowired
    @Qualifier("redisStubService")  // test 환경 redis stub
    RedisService redisService;

    // 실제 redis 테스트 후에 stub로 변경했다
    @Test
    @DisplayName("Redis 기본 테스트(set, get)")
    void redisTest() {
        // given
        String testKey = UUID.randomUUID().toString();
        String testValue = "hello redis";

        // when
        redisService.set(testKey, testValue);
        String getValue = redisService.get(testKey);

        // then
        assertThat(getValue).isEqualTo(testValue);
        redisService.del(testKey);
    }
}
