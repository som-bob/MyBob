package com.my.bob.integration;

import com.my.bob.core.domain.member.repository.BobUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@DisplayName("Test H2 데이터 베이스 확인")
class TestDataBaseInitializationTest {

    @Autowired
    private BobUserRepository bobUserRepository;

    @BeforeEach
    void clearDatabase() {
        bobUserRepository.deleteAll();
    }

    @Test
    @DisplayName("test 환경 Flyway 마이그레이션 테스트")
    void testFlywayMigration() {
        log.info("Success");
    }
}
