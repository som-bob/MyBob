package com.my.bob.core.domain.recipe.dto.request;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import lombok.Data;

import java.util.List;

@Data
public class RecipeCreateDto {
    // 차후 생성에 사용 (TODO)

    private String recipeName;
    private String recipeDescription;
    private Difficulty difficulty;
    private Short cookingTime;

    // 추가 재료 아이디
    private List<RecipeIngredientCreateDto> ingredients;

}
