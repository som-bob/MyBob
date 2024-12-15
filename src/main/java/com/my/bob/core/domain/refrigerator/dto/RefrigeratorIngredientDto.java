package com.my.bob.core.domain.refrigerator.dto;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RefrigeratorIngredientDto {

    private Integer ingredientId;

    private String ingredientName;

    private String addedDate;

    public RefrigeratorIngredientDto(Ingredient ingredient, LocalDate addedDate) {
        this.ingredientId = ingredient.getId();
        this.ingredientName = ingredient.getIngredientName();
        this.addedDate = addedDate.toString();
    }
}
