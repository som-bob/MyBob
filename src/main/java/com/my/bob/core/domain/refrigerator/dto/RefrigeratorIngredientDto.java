package com.my.bob.core.domain.refrigerator.dto;

import com.my.bob.core.domain.recipe.entity.Ingredient;
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

    public RefrigeratorIngredientDto(Ingredient ingredient, LocalDate addedDate) {
        this.ingredientId = ingredient.getId();
        this.ingredientName = ingredient.getIngredientName();
        this.ingredientUrl = ingredient.getImageUrl();
        this.addedDate = addedDate.toString();
    }
}
