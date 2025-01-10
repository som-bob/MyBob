package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("레시피 테스트")
class RecipeServiceTest {
    /**
     * V2__add_test_recipe.sql
     * 상기 파일을 참고 하여 테스트 데이터를 확인합니다.
     */

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @AfterEach
    void cleanUp() {
        recipeIngredientsRepository.deleteAllInBatch();
        recipeRepository.deleteAllInBatch();
        ingredientRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("레시피 조회 테스트")
    void getAllRecipes() {
        // given
        
        // when
        
        // then
    }
}