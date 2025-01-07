package com.my.bob.core.domain.recipe.dto;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.refrigerator.entity.RecipeIngredients;
import lombok.Data;

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

        List<RecipeIngredients> recipeIngredients = recipe.getRecipeIngredients();
        this.ingredients = recipeIngredients
                .stream()
                .map(RecipeIngredients::getIngredient)
                .map(IngredientDto::new)
                .toList();
    }
}
