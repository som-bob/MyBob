package com.my.bob.core.domain.recipe.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RecipeDetailCreateDto {

    private String recipeDetailText;
    private MultipartFile recipeDetailFile;
}
