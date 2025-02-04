package com.my.bob.core.domain.recipe.dto.response;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RecipeListItemDto {
    // 목록 조회용 Dto

    private long recipeId;
    private String recipeName;
    private String recipeDescription;
    private String imageUrl;

    private String servings;
    private Difficulty difficulty;
    private List<IngredientDto> ingredients;

    @Builder
    public RecipeListItemDto(long recipeId, String recipeName, String recipeDescription,
                             String imageUrl,
                             String servings,
                             Difficulty difficulty,
                             List<IngredientDto> ingredients) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.imageUrl = imageUrl;
        this.servings = servings;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
    }
}
