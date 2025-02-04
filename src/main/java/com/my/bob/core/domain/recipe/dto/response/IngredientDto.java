package com.my.bob.core.domain.recipe.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientDto {

    private int id;

    private String ingredientName;

    private String ingredientDescription;

    private String imageUrl;

    @Builder
    public IngredientDto(int id,
                         String ingredientName,
                         String ingredientDescription,
                         String imageUrl) {
        this.id = id;
        this.ingredientName = ingredientName;
        this.ingredientDescription = ingredientDescription;
        this.imageUrl = imageUrl;
    }
}
