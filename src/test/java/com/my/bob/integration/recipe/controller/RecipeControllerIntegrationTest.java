package com.my.bob.integration.recipe.controller;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeIngredients;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import com.my.bob.integration.util.IntegrationTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // 테스트 클래스당 인스턴스 1개만 생성
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("통합 테스트 - 레시피 RecipeController")
public class RecipeControllerIntegrationTest extends IntegrationTestUtils {

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
    private Integer[] ingredientIds;
    private Integer[] ingredientPartIds;
    private final List<Recipe> recipeList = new ArrayList<>();

    @BeforeEach
    @WithAccount("system")
    void setup() {
        token = getTokenFromTestUser();

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
        cleanUp(recipeIngredientsRepository, recipeRepository, ingredientRepository, bobUserRepository);
    }


}
