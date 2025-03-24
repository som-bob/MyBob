package com.my.bob.core.external.redis;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.file.service.FileSaveService;
import com.my.bob.core.domain.recipe.converter.RecipeConverter;
import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeDetailRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import com.my.bob.core.external.redis.service.RecentRecipeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.my.bob.core.domain.recipe.service.helper.RecipeSaveHelper.saveAllRecipeData;
import static com.my.bob.core.external.redis.service.RecentRecipeService.MAX_RECIPES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("레시피 테스트")
@WithAccount("system@system.com")
class RecentRecipeServiceTest {

    @Autowired
    RecentRecipeService recentRecipeService;

    @Autowired
    private FileSaveService fileSaveService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Autowired
    private RecipeDetailRepository recipeDetailRepository;

    private static String randomUser;

    // 테스트 데이터 셋업 (재료 11개, 레시피 11개)
    @BeforeEach
    void setUp() {
        // 기본 재료 저장 (5개), 레시피 저장 (11개)
        saveAllRecipeData(ingredientRepository,
                recipeRepository, recipeDetailRepository, recipeIngredientsRepository,
                fileSaveService,
                5, 11);
        randomUser = String.format("%s@system.com", UUID.randomUUID());
    }

    // 테스트 후 모두 삭제
    @AfterEach
    void cleanUp() {
        recipeIngredientsRepository.deleteAllInBatch();
        recipeDetailRepository.deleteAllInBatch();
        recipeRepository.deleteAllInBatch();
        ingredientRepository.deleteAllInBatch();
        recentRecipeService.clearRecipe(randomUser);    // redis 데이터도 모두 삭제
    }

    @Test
    @DisplayName("최신 레시피 저장 테스트_단일 저장")
    void saveOneRecentRecipe_success() {
        // given
        List<Recipe> recipes = recipeRepository.findAll();
        Optional<Recipe> recipeOptional = recipes.stream().findFirst();
        if (recipeOptional.isEmpty()) {
            fail("fail to find recipe.");
            return;
        }
        Recipe recipe = recipeOptional.get();
        RecipeDto recipeDto = RecipeConverter.convertDto(recipe);

        // when
        recentRecipeService.saveRecentRecipe(randomUser, recipeDto);
        List<RecipeDto> getAllRecipe = recentRecipeService.getRecentRecipes(randomUser);

        // then
        assertThat(getAllRecipe).isNotEmpty().hasSize(1);
        RecipeDto findRecipe = getAllRecipe.stream().findFirst().get();
        assertThat(findRecipe.getRecipeId()).isEqualTo(recipe.getId());
    }

    @Test
    @DisplayName("최신 레시피 저장 테스트_11개 저장")
    void saveManyRecentRecipe_success() {
        // given
        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeDto> recipeList = recipes.stream().map(RecipeConverter::convertDto).toList();
        List<Integer> idList = recipeList.stream().map(RecipeDto::getRecipeId).collect(Collectors.toList());
        Collections.reverse(idList);

        // when
        for (RecipeDto recipeDto : recipeList) {
            recentRecipeService.saveRecentRecipe(randomUser, recipeDto);
        }
        List<RecipeDto> getAllRecipe = recentRecipeService.getRecentRecipes(randomUser);

        // then
        // 최대 개수까지만 저장되어야 하며, 아이디가 후순부터 들어가 있는지 확인
        assertThat(getAllRecipe).isNotEmpty().hasSize(MAX_RECIPES);
        for (int i = 0; i < MAX_RECIPES; i++) {
            assertThat(getAllRecipe.get(i).getRecipeId()).isEqualTo(idList.get(i));
        }
    }



}