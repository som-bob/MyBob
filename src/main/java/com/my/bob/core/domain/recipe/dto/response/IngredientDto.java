package com.my.bob.core.domain.recipe.dto.response;

import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class IngredientDto {

    private int id;

    private String ingredientName;

    private String ingredientDescription;

    private String imageUrl;

    public IngredientDto(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.ingredientName = ingredient.getIngredientName();
        this.ingredientDescription = ingredient.getIngredientDescription();
        this.imageUrl = Optional.ofNullable(ingredient.getFile())
                .map(BobFile::getFileUrl)
                .orElse(null);
    }
}
