package com.my.bob.core.domain.recipe.service;

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
import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("레시피 테스트")
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    private Integer[] ingredientIds;
    private Integer[] ingredientPartIds;
    private final List<Recipe> recipeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        // 기본 재료 3개 이상 저장
        Ingredient i1 = saveIngredient("나_테스트 재료");
        Ingredient i2 = saveIngredient("가_테스트 재료");
        Ingredient i3 = saveIngredient("다_테스트 재료");
        Ingredient i4 = saveIngredient("라_테스트 재료");
        Ingredient i5 = saveIngredient("마_테스트 재료");
        ingredientIds = new Integer[]{i1.getId(), i2.getId(), i3.getId(), i4.getId(), i5.getId()};

        // 레시피 저장
        // 1번 레시피, 재료 1, 2, 3, 4, 5
        Recipe r1 = saveRecipe("1번 레시피", "1번 테스트 레시피", Difficulty.ANYONE, i1, i2, i3, i4, i5);
        recipeList.add(r1);
        // 2번 레시피, 재료 1, 5
        Recipe r2 = saveRecipe("2번 레시피", "2번 테스트 레시피", Difficulty.BEGINNER, i1, i5);
        recipeList.add(r2);
        // 3번 레피시, 재료 2, 4
        Recipe r3 = saveRecipe("3번 레시피", "3번 테스트 레시피", Difficulty.BEGINNER, i2, i4);
        recipeList.add(r3);

        // 조회 테스트를 위한 id list
        ingredientPartIds = new Integer[]{i2.getId(), i4.getId()};
    }

    private Ingredient saveIngredient(String ingredientName) {
        Ingredient ingredient = new Ingredient(ingredientName);
        return ingredientRepository.save(ingredient);
    }

    private Recipe saveRecipe(String recipeName, String recipeDescription, Difficulty difficulty,
                              Ingredient... ingredients) {
        Recipe recipe = new Recipe(recipeName, recipeDescription, difficulty, (short) 30);
        recipeRepository.save(recipe);
        for (Ingredient ingredient : ingredients) {
            saveRecipeIngredient(recipe, ingredient);
        }

        return recipe;
    }

    private void saveRecipeIngredient(Recipe recipe, Ingredient ingredient) {
        RecipeIngredients recipeIngredients = new RecipeIngredients(recipe, ingredient, "재료 양");
        recipeIngredientsRepository.save(recipeIngredients);
    }


    @AfterEach
    void cleanUp() {
        recipeIngredientsRepository.deleteAllInBatch();
        recipeRepository.deleteAllInBatch();
        ingredientRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("레시피 조회 테스트 1 - 조회 조건 없음")
    void getAllRecipesNoCondition() {
        // given
        RecipeSearchDto dto = new RecipeSearchDto();
        PageRequest pageRequest = PageRequest.of(0, recipeList.size());

        // when
        Page<RecipeListItemDto> page = recipeService.getRecipes(pageRequest, dto);

        // then
        assertThat(page.getTotalElements()).isEqualTo(recipeList.size());
        assertThat(page.getContent()).isNotEmpty();
    }


    @Test
    @DisplayName("레시피 조회 테스트 2 - 레시피 이름으로 조회")
    void getAllRecipeByRecipeName() {
        // given
        RecipeSearchDto dto = new RecipeSearchDto();
        dto.setRecipeName("1번 레시피");
        PageRequest pageRequest = PageRequest.of(0, recipeList.size());

        // when
        Page<RecipeListItemDto> page = recipeService.getRecipes(pageRequest, dto);

        // then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).isNotEmpty();
        assertThat(page.getContent().get(0).getRecipeName()).isEqualTo(dto.getRecipeName());
    }

    @Test
    @DisplayName("레시피 조회 테스트 3 - 레시피 설명으로 조회")
    void getAllRecipeByRecipeDescription() {
        // given
        RecipeSearchDto dto = new RecipeSearchDto();
        dto.setRecipeDescription("1번 테스트 레시피");
        PageRequest pageRequest = PageRequest.of(0, recipeList.size());

        // when
        Page<RecipeListItemDto> page = recipeService.getRecipes(pageRequest, dto);

        // then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).isNotEmpty();
        assertThat(page.getContent().get(0).getRecipeName()).isEqualTo("1번 레시피");
    }

    @Test
    @DisplayName("레시피 조회 테스트 4 - 난이도로 조회")
    void getAllRecipeByDifficulty() {
        // given
        RecipeSearchDto dto = new RecipeSearchDto();
        dto.setDifficulty(Difficulty.BEGINNER);
        PageRequest pageRequest = PageRequest.of(0, recipeList.size());

        // when
        Page<RecipeListItemDto> page = recipeService.getRecipes(pageRequest, dto);

        // then
        assertThat(page.getTotalElements()).isPositive();
        assertThat(page.getContent())
                .isNotEmpty()
                .allSatisfy(recipeDto -> assertThat(recipeDto.getDifficulty()).isEqualTo(Difficulty.BEGINNER));
    }

    @Test
    @DisplayName("레시피 조회 테스트 5 - 재료로 조회 (1)")
    @Description("모든 재료로 조회할 경우, 모든 레시피가 조회된다.")
    void getAllRecipeByIngredients() {
        // given
        RecipeSearchDto dto = new RecipeSearchDto();
        List<Integer> ingredientIdList = List.of(ingredientIds);
        dto.setIngredientIds(ingredientIdList);
        PageRequest pageRequest = PageRequest.of(0, recipeList.size());

        // when
        Page<RecipeListItemDto> page = recipeService.getRecipes(pageRequest, dto);

        // then
        assertThat(page.getTotalElements()).isPositive();
        assertThat(page.getContent())
                .isNotEmpty()
                .allSatisfy(recipeDto ->
                        assertThat(recipeDto.getIngredients().stream().map(IngredientDto::getId))
                                .anyMatch(ingredientIdList::contains)
                );
    }

    @Test
    @DisplayName("레시피 조회 테스트 6 - 재료로 조회 (2)")
    void getAllRecipeByPieceIngredient() {
        // given
        RecipeSearchDto dto = new RecipeSearchDto();
        List<Integer> ingredientIdList = List.of(ingredientPartIds);
        dto.setIngredientIds(ingredientIdList);
        PageRequest pageRequest = PageRequest.of(0, recipeList.size());

        // when
        Page<RecipeListItemDto> page = recipeService.getRecipes(pageRequest, dto);

        // then
        assertThat(page.getTotalElements()).isPositive();
        assertThat(page.getContent())
                .isNotEmpty()
                .allSatisfy(recipeDto ->
                        assertThat(recipeDto.getIngredients().stream().map(IngredientDto::getId))
                                .anyMatch(ingredientIdList::contains)
                );
    }
}