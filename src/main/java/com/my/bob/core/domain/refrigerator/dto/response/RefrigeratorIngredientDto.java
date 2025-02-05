package com.my.bob.core.domain.refrigerator.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefrigeratorIngredientDto {
    // 조회용

    private Integer ingredientId;   // refrigerator_ingredient_id

    private String ingredientName;

    private String ingredientUrl;

    private String addedDate;

    @Builder
    public RefrigeratorIngredientDto(Integer ingredientId, String ingredientName, String ingredientUrl,
                                     String addedDate) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.ingredientUrl = ingredientUrl;
        this.addedDate = addedDate;
    }
}
