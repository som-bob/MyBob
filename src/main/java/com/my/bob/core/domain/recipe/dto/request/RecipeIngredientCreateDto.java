package com.my.bob.core.domain.recipe.dto.request;

import lombok.Data;

@Data
public class RecipeIngredientCreateDto {
    // 차후 생성에 사용 (TODO)

    private Integer ingredientId;
    private String amount;
}
