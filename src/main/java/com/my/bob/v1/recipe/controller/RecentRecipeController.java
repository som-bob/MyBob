package com.my.bob.v1.recipe.controller;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.external.redis.service.RecentRecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recent/recipe")
public class RecentRecipeController {

    private final RecentRecipeService recentRecipeService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<RecipeDto>>> getAll(Principal principal) {
        String email = principal.getName();
        List<RecipeDto> recentRecipes = recentRecipeService.getRecentRecipes(email);

        return ResponseEntity.ok(new ResponseDto<>(recentRecipes));
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto<Void>> delete(Principal principal) {
        String email = principal.getName();
        recentRecipeService.clearRecipe(email);

        return ResponseEntity.noContent().build();
    }
}
