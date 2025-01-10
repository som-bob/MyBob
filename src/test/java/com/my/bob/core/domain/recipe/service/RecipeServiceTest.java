package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    // 저장한 재료 map
    private Map<Integer, Ingredient> ingredientMap = new LinkedHashMap<>();

    @BeforeEach
    void setUp() {
        // 재료, 레시피 추가
        // '0~5_재료' 추가
        new HashMap<>();
        for (int ingredientNum = 0; ingredientNum < 5; ingredientNum++) {
            Ingredient ingredient = saveNewIngredient(ingredientNum + "_재료");
            ingredientMap.put(ingredientNum, ingredient);
        }
    }

    @AfterEach
    void cleanUp() {
        recipeIngredientsRepository.deleteAllInBatch();
        recipeRepository.deleteAllInBatch();
        ingredientRepository.deleteAllInBatch();
    }

    private Ingredient saveNewIngredient(String ingredientName) {
        return ingredientRepository.save(new Ingredient(ingredientName));
    }

    @Test
    @DisplayName("레시피 조회 테스트")
    void getAllRecipes() {
        // given
        
        // when
        
        // then
    }
}