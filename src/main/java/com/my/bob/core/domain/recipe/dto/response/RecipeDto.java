package com.my.bob.core.domain.recipe.dto.response;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class RecipeDto {

    private long recipeId;

    private String recipeName;
    private String recipeDescription;

    private String servings;

    private Difficulty difficulty;

    private int cookingTime;

    private String recipeFileUrl;

    private List<RecipeIngredientDto> ingredients = new LinkedList<>();

    private List<RecipeDetailDto> details = new LinkedList<>();

    private String regDate;
    private String regId;
    private String modDate;
    private String modId;
}
