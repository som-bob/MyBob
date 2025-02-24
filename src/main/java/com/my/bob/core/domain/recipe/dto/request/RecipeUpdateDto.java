package com.my.bob.core.domain.recipe.dto.request;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RecipeUpdateDto {
    @NotBlank(message = ErrorMessage.EMPTY_RECIPE_NAME)
    private String recipeName;

    private String recipeDescription;

    @NotNull(message = ErrorMessage.NEED_DIFFICULTY)
    private Difficulty difficulty;

    @NotBlank(message = ErrorMessage.NEED_SERVINGS)
    private String servings;

    private Short cookingTime = -1;

    private boolean isFileChange = false;

    private MultipartFile recipeFile;

    // 추가 재료 리스트 (기존 재료를 모두 삭제 하고 추가 한다)
    private List<RecipeIngredientCreateDto> ingredients;

    // 기존 레시피 상세를 업데이트 한다
    private List<RecipeDetailUpdateDto> details;
}
