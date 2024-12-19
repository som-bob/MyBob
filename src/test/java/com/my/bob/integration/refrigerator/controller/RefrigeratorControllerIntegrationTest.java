package com.my.bob.integration.refrigerator.controller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.dto.JoinUserDto;
import com.my.bob.core.domain.member.dto.LoginDto;
import com.my.bob.core.domain.member.dto.TokenDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.exception.NonExistentUserException;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.member.service.JoinService;
import com.my.bob.core.domain.member.service.LoginService;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // 테스트 클래스당 인스턴스 1개만 생성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("통합 테스트 - 냉장고 RefrigeratorController")
class RefrigeratorControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JoinService joinService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BobUserRepository bobUserRepository;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    private final String BASE_URL = "/api/v1/refrigerator";

    private final String email = "test__user@test.com";
    private final String password = "<PASSWORD1234>!";
    private String token;

    @BeforeEach
    void setUpDatabase() throws DuplicateUserException, NonExistentUserException {
        JoinUserDto dto = new JoinUserDto();
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setNickName("Test User");
        joinService.joinMember(dto);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);
        TokenDto tokenDto = loginService.login(loginDto);
        token = tokenDto.getAccessToken();
    }

    @AfterEach
    void clearDatabase() {
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

    @Test
    @DisplayName("냉장고 생성 - 실패(이미 존재 하는 냉장고 재요청)")
    void createRefrigerator_fail_ExistAlreadyRefrigerator() {
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("테스트 냉장고");
        assertTrue(bobUserRepository.findOneByEmail(email).isPresent());

        // 첫번째 시도 성공
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

        // 두번째 시도 실패
        webTestClient.post()
                .uri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ResponseDto.class)
                .value(responseDto -> {
                    assertEquals(ResponseDto.FailCode.R_00001.name(), responseDto.getErrorCode());
                    assertEquals("FAIL", responseDto.getStatus());
                });
    }

}