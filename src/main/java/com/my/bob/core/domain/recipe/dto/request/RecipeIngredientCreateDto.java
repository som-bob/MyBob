package com.my.bob.core.domain.recipe.dto.request;

import lombok.Data;

@Data
public class RecipeIngredientCreateDto {
    private Integer ingredientId;
    private String amount;
}
