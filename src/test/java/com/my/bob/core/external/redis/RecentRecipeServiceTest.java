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

import java.util.List;
import java.util.Optional;

import static com.my.bob.core.domain.recipe.service.helper.RecipeSaveHelper.saveAllRecipeData;
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

    private final String systemUser = "system@system.com";

    // 테스트 데이터 셋업 (재료 11개, 레시피 11개)
    @BeforeEach
    void setUp() {
        // 기본 재료 저장 (5개), 레시피 저장 (11개)
        saveAllRecipeData(ingredientRepository,
                recipeRepository, recipeDetailRepository, recipeIngredientsRepository,
                fileSaveService,
                5, 11);
    }

    // 테스트 후 모두 삭제
    @AfterEach
    void cleanUp() {
        recipeIngredientsRepository.deleteAllInBatch();
        recipeDetailRepository.deleteAllInBatch();
        recipeRepository.deleteAllInBatch();
        ingredientRepository.deleteAllInBatch();
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
        recentRecipeService.saveRecentRecipe(systemUser, recipeDto);
        List<RecipeDto> getAllRecipe = recentRecipeService.getRecentRecipes(systemUser);
        recentRecipeService.clearRecipe(systemUser);

        // then
        assertThat(getAllRecipe).isNotEmpty().hasSize(1);
        RecipeDto findRecipe = getAllRecipe.stream().findFirst().get();
        assertThat(findRecipe.getRecipeId()).isEqualTo(recipe.getId());
    }

}