package com.my.bob.integration.recipe.controller;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.base.dto.PageResponse;
import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.dto.request.RecipeSearchDto;
import com.my.bob.core.domain.recipe.dto.response.IngredientDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeListItemDto;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeIngredients;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
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
@DisplayName("통합 테스트 - 레시피 RecipeController")
class RecipeControllerIntegrationTest extends IntegrationTestUtils {
    // 현재 레시피 목록 조회까지 테스트 코드 작성 완료

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BobUserRepository bobUserRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    private final String baseUrl = "/api/v1/recipe";
    private String token;

    @BeforeEach
    @WithAccount("system")
    void setup() {
        token = getTokenFromTestUser();
    }

    @AfterEach
    void cleanUp() {
        cleanUp(recipeIngredientsRepository, recipeRepository, ingredientRepository, bobUserRepository);
    }

    @Test
    @DisplayName("재료 조회 - 성공(빈 검색 조건)")
    void getAllRecipe_success_emptySearch() {
        // given
        // 기본 재료 3개 이상 저장
        Ingredient i1 = saveIngredient("나_테스트 재료");
        Ingredient i2 = saveIngredient("가_테스트 재료");
        Ingredient i3 = saveIngredient("다_테스트 재료");
        Ingredient i4 = saveIngredient("라_테스트 재료");
        Ingredient i5 = saveIngredient("마_테스트 재료");

        // 레시피 저장
        saveRecipe("1번 레시피", "1번 테스트 레시피", Difficulty.ANYONE, i1, i2, i3, i4, i5);  // 1번 레시피, 재료 1, 2, 3, 4, 5
        saveRecipe("2번 레시피", "2번 테스트 레시피", Difficulty.BEGINNER, i1, i5);    // 2번 레시피, 재료 1, 5
        saveRecipe("3번 레시피", "3번 테스트 레시피", Difficulty.BEGINNER, i2, i4);    // 3번 레피시, 재료 2, 4

        RecipeSearchDto dto = new RecipeSearchDto();

        // when & then
        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
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
        String recipeName = "1번 레시피";
        RecipeSearchDto dto = new RecipeSearchDto();
        dto.setRecipeName("1번 레시피");

        // 기본 재료 3개 이상 저장
        Ingredient i1 = saveIngredient("나_테스트 재료");
        Ingredient i2 = saveIngredient("가_테스트 재료");
        Ingredient i3 = saveIngredient("다_테스트 재료");
        Ingredient i4 = saveIngredient("라_테스트 재료");
        Ingredient i5 = saveIngredient("마_테스트 재료");

        // 레시피 저장
        saveRecipe(recipeName, "1번 테스트 레시피", Difficulty.ANYONE, i1, i2, i3, i4, i5);  // 1번 레시피, 재료 1, 2, 3, 4, 5
        saveRecipe("2번 레시피", "2번 테스트 레시피", Difficulty.BEGINNER, i1, i5);    // 2번 레시피, 재료 1, 5
        saveRecipe("3번 레시피", "3번 테스트 레시피", Difficulty.BEGINNER, i2, i4);    // 3번 레피시, 재료 2, 4

        // when & then
        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
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
        // given
        // 기본 재료 3개 이상 저장
        Ingredient i1 = saveIngredient("나_테스트 재료");
        Ingredient i2 = saveIngredient("가_테스트 재료");
        Ingredient i3 = saveIngredient("다_테스트 재료");
        Ingredient i4 = saveIngredient("라_테스트 재료");
        Ingredient i5 = saveIngredient("마_테스트 재료");

        // 레시피 저장
        saveRecipe("1번 레시피", "1번 테스트 레시피", Difficulty.ANYONE, i1, i2, i3, i4, i5);  // 1번 레시피, 재료 1, 2, 3, 4, 5
        saveRecipe("2번 레시피", "2번 테스트 레시피", Difficulty.BEGINNER, i1, i5);    // 2번 레시피, 재료 1, 5
        saveRecipe("3번 레시피", "3번 테스트 레시피", Difficulty.BEGINNER, i2, i4);    // 3번 레피시, 재료 2, 4

        /* 모든 재료로 검색 - 모든 레시피가 조회되어야 함 */
        List<Integer> allIngredientIds = List.of(i1.getId(), i2.getId(), i3.getId(), i4.getId(), i5.getId());
        RecipeSearchDto recipeSearchDto = new RecipeSearchDto();
        recipeSearchDto.setIngredientIds(allIngredientIds);

        // when & then
        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(recipeSearchDto)
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
                        assertThat(ingredientIdList).allMatch(allIngredientIds::contains);
                    });
                });

        /* 일부 재료로 검색 - 한 개 레시피가 조회 되어야 함 */
        List<Integer> partIngredientIds = List.of(i1.getId(), i5.getId());
        RecipeSearchDto recipeSearchDto2 = new RecipeSearchDto();
        recipeSearchDto2.setIngredientIds(partIngredientIds);

        // when & then
        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(recipeSearchDto2)
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
                        assertThat(ingredientIdList).allMatch(partIngredientIds::contains);
                    });
                });
    }

    @Test
    @DisplayName("재료 검색 - 성공 (난이도로 검색)")
    void getAllRecipe_success_difficultySearch() {
        // given
        Difficulty difficulty = Difficulty.BEGINNER;

        // 기본 재료 3개 이상 저장
        Ingredient i1 = saveIngredient("나_테스트 재료");
        Ingredient i2 = saveIngredient("가_테스트 재료");
        Ingredient i3 = saveIngredient("다_테스트 재료");
        Ingredient i4 = saveIngredient("라_테스트 재료");
        Ingredient i5 = saveIngredient("마_테스트 재료");

        // 레시피 저장
        saveRecipe("1번 레시피", "1번 테스트 레시피", Difficulty.ANYONE, i1, i2, i3, i4, i5);  // 1번 레시피, 재료 1, 2, 3, 4, 5
        saveRecipe("2번 레시피", "2번 테스트 레시피", difficulty, i1, i5);    // 2번 레시피, 재료 1, 5
        saveRecipe("3번 레시피", "3번 테스트 레시피", difficulty, i2, i4);    // 3번 레피시, 재료 2, 4

        RecipeSearchDto dto = new RecipeSearchDto();
        dto.setDifficulty(difficulty);

        // when & then
        webTestClient.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .bodyValue(dto)
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


    private Ingredient saveIngredient(String ingredientName) {
        Ingredient ingredient = new Ingredient(ingredientName);
        return ingredientRepository.save(ingredient);
    }

    private void saveRecipe(String recipeName, String recipeDescription, Difficulty difficulty,
                            Ingredient... ingredients) {
        Recipe recipe = new Recipe(recipeName, recipeDescription, difficulty, "인분", (short) 30);
        recipeRepository.save(recipe);
        for (Ingredient ingredient : ingredients) {
            saveRecipeIngredient(recipe, ingredient);
        }
    }

    private void saveRecipeIngredient(Recipe recipe, Ingredient ingredient) {
        RecipeIngredients recipeIngredients = new RecipeIngredients(recipe, ingredient, "재료 양");
        recipeIngredientsRepository.save(recipeIngredients);
    }

}
