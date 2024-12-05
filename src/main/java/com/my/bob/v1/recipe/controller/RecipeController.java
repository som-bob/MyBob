package com.my.bob.v1.recipe.controller;

import com.my.bob.core.domain.recipe.dto.RecipeTitleDto;
import com.my.bob.v1.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    // 레시피 리스트 조회
    @GetMapping
    public Page<RecipeTitleDto> getRecipe(){
        // TODO

        return null;
    }
}
