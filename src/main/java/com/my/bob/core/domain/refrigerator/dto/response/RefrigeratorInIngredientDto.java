package com.my.bob.core.domain.refrigerator.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class RefrigeratorInIngredientDto {

    private Integer ingredientId;

    private String ingredientName;

    @Builder
    public RefrigeratorInIngredientDto(Integer ingredientId, String ingredientName) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
    }
}
