package com.my.bob.core.domain.recipe.dto.response;

import lombok.Data;

@Data
public class RecipeIngredientDto {

    private long detailIngredientId;

    private String ingredientDetailName;
    private String amount;

    private String regDate;
    private String regId;
    private String modDate;
    private String modId;
}
