package com.my.bob.integration.recipe.controller;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.base.dto.PageResponse;
import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.file.service.FileSaveService;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.dto.response.IngredientDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeListItemDto;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeDetailRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import com.my.bob.core.external.redis.service.RecentRecipeService;
import com.my.bob.integration.util.IntegrationTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static com.my.bob.core.domain.recipe.service.helper.RecipeSaveHelper.saveIngredient;
import static com.my.bob.core.domain.recipe.service.helper.RecipeSaveHelper.saveRecipe;
import static com.my.bob.integration.common.IntegrationTestResponseValidator.assertSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // 테스트 클래스당 인스턴스 1개만 생성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("통합 테스트 - 레시피 RecipeController")
class RecipeControllerIntegrationTest extends IntegrationTestUtils {
    // 현재 레시피 목록 조회까지 테스트 코드 작성 완료

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BobUserRepository bobUserRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeDetailRepository recipeDetailRepository;

    @Autowired
    RecipeIngredientsRepository recipeIngredientsRepository;

    @Autowired
    RecentRecipeService recentRecipeService;

    @Autowired
    FileSaveService fileSaveService;

    private final String baseUrl = "/api/v1/recipe";
    private String token;

    private static List<Integer> allIngredientsId;
    private static List<Integer> partIngredientsId;
    private static final String RECIPE_NAME = "1번 레시피";

    @BeforeEach
    @WithAccount("system")
    void setup() {
        token = registerAndGetTokenFromTestUser();

        // 기본 재료 3개 이상 저장
        Ingredient i1 = saveIngredient(ingredientRepository, "나_테스트 재료");
        Ingredient i2 = saveIngredient(ingredientRepository, "가_테스트 재료");
        Ingredient i3 = saveIngredient(ingredientRepository, "다_테스트 재료");
        Ingredient i4 = saveIngredient(ingredientRepository, "라_테스트 재료");
        Ingredient i5 = saveIngredient(ingredientRepository, "마_테스트 재료");

        // 레시피 저장
        saveRecipe(recipeRepository, recipeDetailRepository, recipeIngredientsRepository, fileSaveService,
                RECIPE_NAME, "1번 테스트 레시피", Difficulty.ANYONE, i1, i2, i3, i4, i5);  // 1번 레시피, 재료 1, 2, 3, 4, 5
        allIngredientsId = List.of(i1.getId(), i2.getId(), i3.getId(), i4.getId(), i5.getId());

        saveRecipe(recipeRepository, recipeDetailRepository, recipeIngredientsRepository, fileSaveService,
                "2번 레시피", "2번 테스트 레시피", Difficulty.BEGINNER, i1, i5);    // 2번 레시피, 재료 1, 5
        partIngredientsId = List.of(i1.getId(), i5.getId());

        saveRecipe(recipeRepository, recipeDetailRepository, recipeIngredientsRepository, fileSaveService,
                "3번 레시피", "3번 테스트 레시피", Difficulty.BEGINNER, i2, i4);    // 3번 레피시, 재료 2, 4
    }

    @AfterEach
    void cleanUp() {
        cleanUp(recipeIngredientsRepository, recipeDetailRepository, recipeRepository, ingredientRepository, bobUserRepository);
        recentRecipeService.clearRecipe(getTestUserEmail());
    }

    @Test
    @DisplayName("재료 조회 - 성공(빈 검색 조건)")
    void getAllRecipe_success_emptySearch() {
        // when & then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(baseUrl).build())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<PageResponse<RecipeListItemDto>>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    PageResponse<RecipeListItemDto> responseDtoData = responseDto.getData();
                    assertThat(responseDtoData.getContent()).isNotEmpty();
                });
    }

    @Test
    @DisplayName("재료 조회 - 성공 (재료 이름으로 검색)")
    void getAllRecipe_success_recipeNameSearch() {
        // given
        String recipeName = RECIPE_NAME;

        // when & then
        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(baseUrl).queryParam("recipeName", recipeName).build())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<PageResponse<RecipeListItemDto>>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    PageResponse<RecipeListItemDto> responseDtoData = responseDto.getData();
                    assertThat(responseDtoData.getContent()).isNotEmpty();
                    assertThat(responseDtoData.getContent().get(0).getRecipeName()).isEqualTo(recipeName);
                });
    }

    @Test
    @DisplayName("재료 검색 - 성공 (재료 리스트로 검색)")
    void getAllRecipe_success_allIngredientIdsSearch() {
        /* 모든 재료로 검색 - 모든 레시피가 조회되어야 함 */

        // when & then
        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(baseUrl).queryParam("ingredientIds", allIngredientsId).build())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<PageResponse<RecipeListItemDto>>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    PageResponse<RecipeListItemDto> responseDtoData = responseDto.getData();
                    assertThat(responseDtoData.getContent()).isNotEmpty();
                    assertThat(responseDtoData.getContent()).hasSize(3);

                    responseDtoData.getContent().forEach(recipe -> {
                        List<Integer> ingredientIdList = recipe.getIngredients()
                                .stream()
                                .map(IngredientDto::getId).toList();

                        assertThat(ingredientIdList).isNotEmpty();
                        // 각 값이 ingredientIds에 포함되는지 확인
                        assertThat(ingredientIdList).allMatch(allIngredientsId::contains);
                    });
                });

        // when & then
        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(baseUrl).queryParam("ingredientIds", partIngredientsId).build())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<PageResponse<RecipeListItemDto>>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    PageResponse<RecipeListItemDto> responseDtoData = responseDto.getData();
                    assertThat(responseDtoData.getContent()).isNotEmpty();

                    responseDtoData.getContent().forEach(recipe -> {
                        List<Integer> ingredientIdList = recipe.getIngredients()
                                .stream()
                                .map(IngredientDto::getId).toList();

                        assertThat(ingredientIdList).isNotEmpty();
                        assertThat(responseDtoData.getContent()).hasSize(1);
                        // 각 값이 ingredientIds에 포함되는지 확인
                        assertThat(ingredientIdList).allMatch(partIngredientsId::contains);
                    });
                });
    }

    @Test
    @DisplayName("재료 검색 - 성공 (난이도로 검색)")
    void getAllRecipe_success_difficultySearch() {
        // given
        Difficulty difficulty = Difficulty.BEGINNER;

        // when & then
        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(baseUrl).queryParam("difficulty", difficulty.getCode()).build())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<PageResponse<RecipeListItemDto>>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    PageResponse<RecipeListItemDto> responseDtoData = responseDto.getData();
                    assertThat(responseDtoData.getContent()).isNotEmpty();
                    assertThat(responseDtoData.getContent()).hasSize(2);

                    responseDtoData.getContent().forEach(recipe ->
                            assertThat(recipe.getDifficulty()).isEqualTo(difficulty));
                });
    }

    @Test
    @DisplayName("레시피 상세 조회 - 성공")
    void getRecipe_success() {
        // given
        Optional<Recipe> recipeOptional = recipeRepository.findAll().stream().findFirst();
        if (recipeOptional.isEmpty()) {
            fail("fail to find recipe");
            return;
        }
        Recipe recipe = recipeOptional.get();
        Integer recipeId = recipe.getId();

        // when & then
        String url = String.format("%s/%s", baseUrl, recipeId);
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(url).build())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<RecipeDto>>() {
                })
                .value(responseDto -> {
                    assertSuccessResponse(responseDto);

                    RecipeDto recipeDto = responseDto.getData();
                    assertThat(recipeDto).isNotNull();
                    assertThat(recipeDto.getRecipeId()).isEqualTo(recipeId);
                });
    }
}
