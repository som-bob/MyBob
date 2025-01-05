package com.my.bob.core.domain.refrigerator.dto;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RefrigeratorIngredientDto {
    // 조회용

    private Integer ingredientId;

    private String ingredientName;

    private String ingredientUrl;

    private String addedDate;

    public RefrigeratorIngredientDto(RefrigeratorIngredient ingredient, LocalDate addedDate) {
        this.ingredientId = ingredient.getId();
        this.ingredientName = ingredient.getIngredient().getIngredientName();
        this.ingredientUrl = ingredient.getIngredient().getImageUrl();
        this.addedDate = addedDate.toString();
    }
}
