package com.my.bob.v1.refrigerator.controller;

import com.my.bob.core.config.JwtTokenProvider;
import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.dto.TokenDto;
import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.member.service.BobUserService;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("통합 테스트 - 냉장고 RefrigeratorController")
class RefrigeratorControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BobUserService bobUserService;

    @Autowired
    private BobUserRepository bobUserRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    private final String BASE_URL = "/api/v1/refrigerator";

    private final String email = "test__user@test.com";
    private String token;

    @BeforeEach
    void clearDatabase() {
        refrigeratorRepository.deleteAll();
        refrigeratorIngredientRepository.deleteAll();

        this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();

        BobUser testUser = new BobUser(email, "<PASSWORD>", "Test User");
        bobUserService.save(testUser);
        bobUserRepository.flush();

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(testUser.getEmail(), "ROLE_USER");
        token = tokenDto.getAccessToken();
    }

    @AfterEach
    void clear() {
        refrigeratorRepository.deleteAll();
        refrigeratorIngredientRepository.deleteAll();
        bobUserRepository.deleteAll();
    }

    @Test
    @DisplayName("냉장고 생성 - 성공")
    void createRefrigerator_success() {
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("테스트 냉장고");
        assertTrue(bobUserRepository.findOneByEmail(email).isPresent());

        webTestClient.post()
                .uri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertEquals("SUCCESS", responseDto.getStatus());
                    assertNotNull(responseDto.getData());
                    assertFalse(refrigeratorRepository.findAll().isEmpty());
                })
        ;
    }

}