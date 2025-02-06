package com.my.bob.core.domain.recipe.dto.request;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RecipeCreateDto {
    @NotBlank(message = ErrorMessage.EMPTY_RECIPE_NAME)
    private String recipeName;

    private String recipeDescription;

    @NotNull(message = ErrorMessage.NEED_DIFFICULTY)
    private Difficulty difficulty;

    @NotBlank(message = ErrorMessage.NEED_SERVINGS)
    private String servings;

    private Short cookingTime = -1;

    private MultipartFile recipeFile;

    // 추가 재료 리스트
    private List<RecipeIngredientCreateDto> ingredients;

    // 추가 레시피 상세 리스트 (빈 값 허용)
    private List<RecipeDetailCreateDto> recipeDetails;
}
