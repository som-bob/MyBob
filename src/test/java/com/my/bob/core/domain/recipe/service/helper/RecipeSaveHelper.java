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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;

public class RecipeSaveHelper {

    public static Recipe saveRecipe(RecipeRepository recipeRepository,
                                    RecipeDetailRepository recipeDetailRepository,
                                    RecipeIngredientsRepository recipeIngredientsRepository,
                                    FileSaveService fileSaveService,
                                    String recipeName, String recipeDescription, Difficulty difficulty,
                                    Ingredient... ingredients) throws IOException {
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
                                         Recipe recipe, int order, String text) throws IOException {
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
                                             String resourceFileName, FileRoute fileRoute) throws IOException {
        MultipartFile file = ResourceUtil.getFileFromResource(resourceFileName);
        return fileSaveService.uploadAndSaveFile(file, fileRoute);
    }
}
