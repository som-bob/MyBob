package com.my.bob.core.domain.recipe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeIngredientDto {

    private long recipeIngredientId;
    private long ingredientId;

    private String ingredientDetailName;
    private String amount;

    private String regDate;
    private String regId;
    private String modDate;
    private String modId;
}
