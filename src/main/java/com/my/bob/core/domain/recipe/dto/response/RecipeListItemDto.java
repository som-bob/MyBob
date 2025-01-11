package com.my.bob.core.domain.recipe.dto.response;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeIngredients;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

@Data
public class RecipeListItemDto {
    // 목록 조회용 Dto

    private int recipeId;
    private String recipeName;
    private String imageUrl;

    private String servings;
    private Difficulty difficulty;
    private List<IngredientDto> ingredients;

    public RecipeListItemDto(Recipe recipe) {
        this.recipeId = recipe.getId();
        this.recipeName = recipe.getRecipeName();
        this.imageUrl = recipe.getImageUrl();
        this.servings = recipe.getServings();
        this.difficulty = recipe.getDifficulty();

        List<Ingredient> ingredientList = recipe.getRecipeIngredients()
                .stream()
                .map(RecipeIngredients::getIngredient)
                .sorted(Comparator.comparing(Ingredient::getIngredientName))
                .toList();

        this.ingredients = ingredientList
                .stream()
                .map(IngredientDto::new)
                .toList();
    }
}
