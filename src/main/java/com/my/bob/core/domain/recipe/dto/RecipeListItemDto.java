package com.my.bob.core.domain.recipe.dto;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import lombok.Data;

@Data
public class RecipeListItemDto {
    // 목록 조회용 Dto

    private int recipeId;
    private String recipeName;
    private String imageUrl;

    private String servings;
    private Difficulty difficulty;
}
