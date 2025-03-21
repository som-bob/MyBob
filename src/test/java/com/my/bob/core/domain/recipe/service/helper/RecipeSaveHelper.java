package com.my.bob.core.domain.recipe.service.helper;

import com.my.bob.core.domain.file.constant.FileRoute;
import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.file.service.FileSaveService;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeDetail;
import com.my.bob.core.domain.recipe.entity.RecipeIngredients;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeDetailRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import com.my.bob.util.ResourceUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RecipeSaveHelper {

    public static void saveAllRecipeData(IngredientRepository ingredientRepository,
                                         RecipeRepository recipeRepository,
                                         RecipeDetailRepository recipeDetailRepository,
                                         RecipeIngredientsRepository recipeIngredientsRepository,
                                         FileSaveService fileSaveService,
                                         int ingredientCnt,
                                         int recipeCnt) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientCnt; i++) {
            Ingredient ingredient = saveIngredient(ingredientRepository, String.format("%d_테스트 재료", i));
            ingredients.add(ingredient);
        }

        // 레시피 저장 (11개)
        for (int i = 0; i < recipeCnt; i++) {
            Difficulty[] difficulties = Difficulty.values(); // 순차적인 난이도
            saveRecipe(recipeRepository, recipeDetailRepository, recipeIngredientsRepository, fileSaveService,
                    String.format("%d번 레시피", i), String.format("%d번 레시피 설명입니다.", i),
                    difficulties[recipeCnt % difficulties.length],
                    getRandomIngredients(ingredients, new Random().nextInt(ingredients.size())));
        }
    }

    public static Recipe saveRecipe(RecipeRepository recipeRepository,
                                    RecipeDetailRepository recipeDetailRepository,
                                    RecipeIngredientsRepository recipeIngredientsRepository,
                                    FileSaveService fileSaveService,
                                    String recipeName, String recipeDescription, Difficulty difficulty,
                                    Ingredient... ingredients) {
        Recipe recipe = new Recipe(recipeName, recipeDescription, difficulty, "인분", (short) 30);
        BobFile bobFile = uploadAndSaveFile(fileSaveService, "test.png", FileRoute.RECIPE);
        recipe.setRecipeFile(bobFile);
        recipeRepository.save(recipe);

        for (Ingredient ingredient : ingredients) {
            saveRecipeIngredient(recipeIngredientsRepository, recipe, ingredient);
        }

        int recipeDetailOrder = new Random().nextInt(1, 4);
        for (int order = 1; order <= recipeDetailOrder; order++) {
            saveRecipeDetail(recipeDetailRepository, fileSaveService, recipe, order, "%d 순서 입니다.".formatted(order));
        }

        return recipe;
    }

    private static void saveRecipeIngredient(RecipeIngredientsRepository recipeIngredientsRepository,
                                             Recipe recipe, Ingredient ingredient) {
        RecipeIngredients recipeIngredients =
                new RecipeIngredients(recipe, ingredient, ingredient.getIngredientName(), "재료 양");
        recipeIngredientsRepository.save(recipeIngredients);
    }

    private static void saveRecipeDetail(RecipeDetailRepository recipeDetailRepository,
                                         FileSaveService fileSaveService,
                                         Recipe recipe, int order, String text) {
        RecipeDetail recipeDetail = new RecipeDetail(recipe, order, text);
        BobFile bobFile = uploadAndSaveFile(fileSaveService, "test%d.png".formatted(order), FileRoute.RECIPE_DETAIL);
        recipeDetail.setRecipeDetailFile(bobFile);

        recipeDetailRepository.save(recipeDetail);
    }

    public static Ingredient saveIngredient(IngredientRepository ingredientRepository, String ingredientName) {
        Ingredient ingredient = new Ingredient(ingredientName);
        return ingredientRepository.save(ingredient);
    }

    private static BobFile uploadAndSaveFile(FileSaveService fileSaveService,
                                             String resourceFileName, FileRoute fileRoute) {
        try {
            MultipartFile file = ResourceUtil.getFileFromResource(resourceFileName);
            return fileSaveService.uploadAndSaveFile(file, fileRoute);
        } catch (IOException e) {
            return null;
        }
    }

    private static Ingredient[] getRandomIngredients(List<Ingredient> ingredients, int count) {
        if (CollectionUtils.isEmpty(ingredients)) {
            return new Ingredient[]{};
        }

        ArrayList<Ingredient> ingredientList = new ArrayList<>(ingredients);
        Collections.shuffle(ingredientList);

        return ingredientList.subList(0, Math.min(ingredientList.size(), count)).toArray(new Ingredient[0]);
    }
}
