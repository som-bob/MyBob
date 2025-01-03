package com.my.bob.core.domain.recipe.dto;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import lombok.Data;

import java.util.List;

@Data
public class RecipeSearchDto {

    private String recipeName;
    private String recipeDescription;

    private List<Integer> ingredientIds;
    private Difficulty difficulty;      // 난이도
}
