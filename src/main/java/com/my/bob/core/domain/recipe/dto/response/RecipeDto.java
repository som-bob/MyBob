package com.my.bob.core.domain.recipe.dto.response;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeDto {

    private int recipeId;

    private String recipeName;
    private String recipeDescription;

    private String servings;

    private Difficulty difficulty;

    private short cookingTime;

    private String recipeFileUrl;

    private String source;

    private List<RecipeIngredientDto> ingredients;

    private List<RecipeDetailDto> details;

    private String regDate;
    private String regId;
    private String modDate;
    private String modId;

}
