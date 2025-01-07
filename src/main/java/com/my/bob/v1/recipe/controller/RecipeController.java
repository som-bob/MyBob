package com.my.bob.v1.recipe.controller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.recipe.dto.RecipeListItemDto;
import com.my.bob.core.domain.recipe.dto.RecipeSearchDto;
import com.my.bob.core.domain.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    // 레시피 리스트 조회
    @PostMapping
    public ResponseEntity<ResponseDto<Page<RecipeListItemDto>>> getRecipe(Pageable pageable,
                                                                          @RequestBody RecipeSearchDto dto) {
        Page<RecipeListItemDto> recipes = recipeService.getRecipes(pageable, dto);

        return ResponseEntity.ok(new ResponseDto<>(recipes));
    }
}
