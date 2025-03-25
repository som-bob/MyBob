package com.my.bob.integration.recipe.controller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.file.service.FileSaveService;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeDetailRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import com.my.bob.core.external.redis.service.RecentRecipeService;
import com.my.bob.integration.common.IntegrationTestResponseValidator;
import com.my.bob.integration.util.IntegrationTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.my.bob.core.domain.recipe.service.helper.RecipeSaveHelper.saveAllRecipeData;
import static com.my.bob.integration.common.IntegrationTestResponseValidator.assertSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // 테스트 클래스당 인스턴스 1개만 생성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("통합 테스트 - 최근 레시피 RecentRecipeController")
class RecentRecipeControllerIntegrationTest extends IntegrationTestUtils {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BobUserRepository bobUserRepository;

    @Autowired
    RecentRecipeService recentRecipeService;

    @Autowired
    FileSaveService fileSaveService;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeIngredientsRepository recipeIngredientsRepository;

    @Autowired
    RecipeDetailRepository recipeDetailRepository;

    private final String recipeBaseUrl = "/api/v1/recipe";
    private final String recentRecipeBaseUrl = "/api/v1/recent/recipe";
    private static String token;

    // 테스트 데이터 셋업 (재료 11개, 레시피 11개)
    @BeforeEach
    void setUp() {
        token = registerAndGetTokenFromTestUser();

        // 기본 재료 저장 (5개), 레시피 저장 (11개)
        saveAllRecipeData(ingredientRepository,
                recipeRepository, recipeDetailRepository, recipeIngredientsRepository,
                fileSaveService, 5, 11);
    }

    // 테스트 후 모두 삭제
    @AfterEach
    void cleanUp() {
        cleanUp(recipeIngredientsRepository, recipeDetailRepository, recipeRepository, ingredientRepository, bobUserRepository);
        recentRecipeService.clearRecipe(getTestUserEmail());
    }

    @Test
    @DisplayName("다양한 레시피 상세 조회 후 최신 레시피 리스트 조회")
    void getAllRecentRecipe() {
        // given
        List<Recipe> recipes = recipeRepository.findAll();
        List<Integer> recipeIds = recipes.stream().map(Recipe::getId).toList();
        // 총 11개의 recipe 조회
        for (Integer recipeId : recipeIds) {
            String url = String.format("%s/%s", recipeBaseUrl, recipeId);
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.path(url).build())
                    .header("Authorization", "Bearer " + token)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(new ParameterizedTypeReference<ResponseDto<RecipeDto>>() {
                    })
                    .value(IntegrationTestResponseValidator::assertSuccessResponse);
        }

        // when & then
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                webTestClient.get()
                        .uri(recentRecipeBaseUrl)
                        .header("Authorization", "Bearer " + token)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(new ParameterizedTypeReference<ResponseDto<List<RecipeDto>>>() {
                        })
                        .value(responseDto -> {
                            assertSuccessResponse(responseDto);
                            List<RecipeDto> recentRecipList = responseDto.getData();
                            assertThat(recentRecipList).hasSize(RecentRecipeService.MAX_RECIPES);
                        }));
    }

    @Test
    @DisplayName("다양한 레시피 상세 조회 후 최신 레시피 리스트 삭제")
    void deleteAllRecentRecipes() {
        // given
        List<Recipe> recipes = recipeRepository.findAll();
        List<Integer> recipeIds = recipes.stream().map(Recipe::getId).toList();
        // 총 11개의 recipe 조회
        for (Integer recipeId : recipeIds) {
            String url = String.format("%s/%s", recipeBaseUrl, recipeId);
            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.path(url).build())
                    .header("Authorization", "Bearer " + token)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(new ParameterizedTypeReference<ResponseDto<RecipeDto>>() {
                    })
                    .value(IntegrationTestResponseValidator::assertSuccessResponse);
        }

        // when & then
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                webTestClient.delete()
                        .uri(recentRecipeBaseUrl)
                        .header("Authorization", "Bearer " + token)
                        .exchange()
                        .expectStatus()
                        .isNoContent());
    }
}
