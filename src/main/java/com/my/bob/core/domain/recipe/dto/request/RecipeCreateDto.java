package com.my.bob.core.domain.recipe.dto.request;

import com.my.bob.core.domain.recipe.contants.Difficulty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RecipeCreateDto {
    private String recipeName;
    private String recipeDescription;
    private Difficulty difficulty;
    private Short cookingTime;
    private MultipartFile recipeFile;

    // 추가 재료 아이디
    private List<RecipeIngredientCreateDto> ingredients;

    // 추가 재료 상세 리스트
    private List<RecipeDetailCreateDto> recipeDetails;
}
