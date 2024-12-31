package com.my.bob.core.domain.recipe.dto;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "재료 정보를 담고 있는 DTO") // Swagger 설명
public class IngredientDto {

    @Schema(description = "재료 ID", example = "1")
    private int id;

    @Schema(description = "재료 이름", example = "우유")
    private String ingredientName;

    @Schema(description = "재료 설명", example = "요리에 자주 쓰이는 재료입니다.")
    private String ingredientDescription;

    @Schema(description = "아이콘 이미지 url", example = "https://image.url", nullable = true)
    private String imageUrl;

    public IngredientDto(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.ingredientName = ingredient.getIngredientName();
        this.ingredientDescription = ingredient.getIngredientDescription();
        this.imageUrl = ingredient.getImageUrl();
    }
}
