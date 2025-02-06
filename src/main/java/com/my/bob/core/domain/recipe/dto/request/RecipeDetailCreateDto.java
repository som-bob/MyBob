package com.my.bob.core.domain.recipe.dto.request;

import com.my.bob.core.constants.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RecipeDetailCreateDto {

    @NotBlank(message = ErrorMessage.NEED_RECIPE_DETAIL)
    private String recipeDetailText;

    private MultipartFile recipeDetailFile;
}
