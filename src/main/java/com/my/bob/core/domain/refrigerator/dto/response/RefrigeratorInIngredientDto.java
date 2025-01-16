package com.my.bob.core.domain.refrigerator.dto.response;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import lombok.Data;

@Data
public class RefrigeratorInIngredientDto {

    private Integer ingredientId;

    private String ingredientName;

    public RefrigeratorInIngredientDto(Ingredient ingredient) {
        this.ingredientId = ingredient.getId();
        this.ingredientName = ingredient.getIngredientName();
    }
}
