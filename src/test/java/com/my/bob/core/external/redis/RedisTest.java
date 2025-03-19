package com.my.bob.core.external.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Redis 서비스 테스트")
class RedisTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("Redis 기본 테스트(set, get)")
    void redisTest() {
        // given
        String testKey = UUID.randomUUID().toString();
        String testValue = "hello redis";

        // when
        redisTemplate.opsForValue().set(testKey, testValue);
        String getValue = redisTemplate.opsForValue().get(testKey);

        // then
        assertThat(getValue).isEqualTo(testValue);
        redisTemplate.delete(testKey);
    }
}
