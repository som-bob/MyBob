package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.domain.recipe.dto.request.RecipeCreateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface RecipeSaveService {
    /**
     * 레시피를 저장합니다.
     * @param dto 레시피 저장에 필요한 데이터
     * @param recipeFile 레시피 파일
     * @param recipeDetailsFiles 레시피 순서에 매핑되는 MultipartFile Map. (map key: index start 0)
     * @return 저장된 레시피의 id 값입니다.
     */
    int newRecipe(RecipeCreateDto dto, MultipartFile recipeFile, Map<String, MultipartFile> recipeDetailsFiles);
}
