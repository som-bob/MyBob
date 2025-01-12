package com.my.bob.integration.ingredient.controller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.recipe.dto.response.IngredientDto;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.integration.util.IntegrationTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.my.bob.integration.common.IntegrationTestResponseValidator.assertSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // 테스트 클래스당 인스턴스 1개만 생성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("재료 통합 테스트")
class IngredientControllerIntegrationTest extends IntegrationTestUtils {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private BobUserRepository bobUserRepository;

    private final String baseUrl = "/api/v1/ingredient";
    private String token;

    @BeforeEach
    void setUp() {
        token = getTokenFromTestUser();

        // 기본 재료 저장
        ingredientRepository.save(new Ingredient("나_테스트 재료"));
        ingredientRepository.save(new Ingredient("다_테스트 재료"));
        ingredientRepository.save(new Ingredient("가_테스트 재료"));
    }

    @AfterEach
    void cleanUp(){
        cleanUp(ingredientRepository, bobUserRepository);
    }

    @Test
    @DisplayName("재료 조회 - 성공")
    void getAllIngredient_success() {
        // when & then
        webTestClient.get()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<List<IngredientDto>>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    assertThat(responseDto.getData())
                            .isNotNull()
                            .hasSize(3)
                            .extracting(IngredientDto::getIngredientName)
                            .containsExactly("가_테스트 재료", "나_테스트 재료", "다_테스트 재료");
                });
    }

    @Test
    @DisplayName("재료 조회 - 빈 결과")
    void getAllIngredient_empty() {
        // given
        ingredientRepository.deleteAllInBatch();

        // when & then
        webTestClient.get()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<List<IngredientDto>>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    assertThat(responseDto.getData()).isNotNull().isEmpty();
                });
    }

}
