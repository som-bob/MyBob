package com.my.bob.core.domain.recipe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RecipeSearchDto {

    private String recipeName;
    private String recipeDescription;

    private List<Integer> ingredientIds;
    private String difficulty;      // 난이도
}
