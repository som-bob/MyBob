package com.my.bob.core.domain.recipe.dto.request;

import com.my.bob.core.constants.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecipeIngredientCreateDto {
    @NotNull(message = ErrorMessage.INVALID_REQUEST)
    private Integer ingredientId;

    private String ingredientDetailName;

    private String amount;
}
